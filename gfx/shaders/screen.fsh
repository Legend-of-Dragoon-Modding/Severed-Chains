#version 330 core

// shader inputs
smooth in vec2 vertUv;

// shader outputs
layout(location = 0) out vec4 frag;

// screen image
uniform sampler2D screen;

uniform bool enableCrt;

uniform float time;

uniform float scanlines_opacity;
uniform float scanlines_width;
uniform float grille_opacity;
uniform vec2 resolution; // Set the number of rows and columns the texture will be divided in. Scanlines and grille will make a square based on these values

uniform bool pixelate; // Fill each square ("pixel") with a sampled color, creating a pixel look and a more accurate representation of how a CRT monitor would work.

uniform bool roll;
uniform float roll_speed; // Positive values are down, negative are up
uniform float roll_size;
uniform float roll_variation; // This value is not an exact science. You have to play around with the value to find a look you like. How this works is explained in the code below.
uniform float distort_intensity; // The distortion created by the rolling effect.

uniform float noise_opacity;
uniform float noise_speed; // There is a movement in the noise pattern that can be hard to see first. This sets the speed of that movement.

uniform float static_noise_intensity;

uniform float aberration; // Chromatic aberration, a distortion on each color channel.
uniform float brightness; // When adding scanline gaps and grille the image can get very dark. Brightness tries to compensate for that.
uniform bool discolor; // Add a discolor effect simulating a VHS

uniform float warp_amount; // Warp the texture edges simulating the curved glass of a CRT monitor or old TV.
bool clip_warp = false;

uniform float vignette_intensity; // Size of the vignette, how far towards the middle it should go.
uniform float vignette_opacity;
uniform float bloom_intensity;
uniform float bloom_threshold;
uniform float bloom_radius;
uniform vec4 turn_order_bounds;

// Performs bilinear filtering manually on a texture
vec4 textureBilinear(sampler2D tex, vec2 uv) {
  vec2 size = vec2(textureSize(tex, 0));
  vec2 texel = uv * size - 0.5;
  vec2 f = fract(texel);
  vec2 ip = (floor(texel) + 0.5) / size;
  vec2 one = 1.0 / size;

  vec4 t00 = texture(tex, ip);
  vec4 t10 = texture(tex, ip + vec2(one.x, 0.0));
  vec4 t01 = texture(tex, ip + vec2(0.0, one.y));
  vec4 t11 = texture(tex, ip + one);

  return mix(mix(t00, t10, f.x), mix(t01, t11, f.x), f.y);
}

// The Turn Order UI is too small to be legible below 480P,
// so cheat the pixelation for that section of the screen
vec2 getActiveResolution(vec2 uv) {
  vec2 res = resolution;
  if(pixelate && res.y <= 480.0) {
    if(uv.x >= turn_order_bounds.x && uv.x <= turn_order_bounds.z &&
       uv.y >= turn_order_bounds.y && uv.y <= turn_order_bounds.w) {
      float aspect = res.x / res.y;
      res = vec2(480.0 * aspect * 1.5, 480.0);
    }
  }
  return res;
}

// High-pass filter used to select bright pixels for Bloom effect
vec3 sampleHighPass(sampler2D tex, vec2 uv, vec2 offset, float threshold) {
  vec2 sample_uv = uv + offset;
  if(pixelate) {
    vec2 res = getActiveResolution(sample_uv);
    sample_uv = (floor(sample_uv * res) + 0.5) / res;
  }
  vec3 color = textureBilinear(tex, sample_uv).rgb;
  return max(color - vec3(threshold), vec3(0.0));
}

// Used by the noise function to generate a pseudo random value between 0.0 and 1.0
vec2 random(vec2 uv) {
  uv = vec2(dot(uv, vec2(127.1,311.7)), dot(uv, vec2(269.5,183.3)));
  return -1.0 + 2.0 * fract(sin(uv) * 43758.5453123);
}

// Generate a Perlin noise used by the distortion effects
float noise(vec2 uv) {
  vec2 uv_index = floor(uv);
  vec2 uv_fract = fract(uv);

  vec2 blur = smoothstep(0.0, 1.0, uv_fract);

  return mix(mix(dot(random(uv_index + vec2(0.0,0.0)), uv_fract - vec2(0.0,0.0)),
                 dot(random(uv_index + vec2(1.0,0.0)), uv_fract - vec2(1.0,0.0)), blur.x),
             mix(dot(random(uv_index + vec2(0.0,1.0)), uv_fract - vec2(0.0,1.0)),
                 dot(random(uv_index + vec2(1.0,1.0)), uv_fract - vec2(1.0,1.0)), blur.x), blur.y) * 0.5 + 0.5;
}

// Takes in the UV and warps the edges, creating the spherized effect
vec2 warp(vec2 uv) {
	vec2 delta = uv - 0.5;
	float delta2 = dot(delta.xy, delta.xy);
	float delta4 = delta2 * delta2;
	float delta_offset = delta4 * warp_amount;

	return uv + delta * delta_offset;
}

// Adds a black border to hide stretched pixel created by the warp effect
float border(vec2 uv) {
	float radius = min(warp_amount, 0.08);
	radius = max(min(min(abs(radius * 2.0), abs(1.0)), abs(1.0)), 1e-5);
	vec2 abs_uv = abs(uv * 2.0 - 1.0) - vec2(1.0, 1.0) + radius;
	float dist = length(max(vec2(0.0), abs_uv)) / radius;
	float square = smoothstep(0.96, 1.0, dist);
	return clamp(1.0 - square, 0.0, 1.0);
}

// Adds a vignette shadow to the edges of the image
float vignette(vec2 uv) {
	uv *= 1.0 - uv.xy;
	float vignette = uv.x * uv.y * 15.0;
	return pow(vignette, vignette_intensity * vignette_opacity);
}

void main() {
  frag = vec4(texture(screen, vertUv).rgb, 1.0f);

  if(!enableCrt) {
    return;
  }

  vec2 UV = vertUv;

  vec2 uv = warp(UV); // Warp the uv. uv will be used in most cases instead of UV to keep the warping
  vec2 text_uv = uv;
  vec2 roll_uv = vec2(0.0);
  float rollTime = roll ? time : 0.0;

  vec2 active_res = getActiveResolution(uv);

  // Pixelate the texture based on the given resolution.
  if(pixelate) {
    text_uv = (floor(uv * active_res) + 0.5) / active_res;
  }

  // Create the rolling effect. We need roll_line a bit later to make the noise effect.
  // That is why this runs if roll is true OR noise_opacity is over 0.
  float roll_line = 0.0;
  if(roll || noise_opacity > 0.0) {
    // Create the areas/lines where the texture will be distorted.
    roll_line = smoothstep(0.3, 0.9, sin(uv.y * roll_size - (rollTime * roll_speed) ) );
    // Create more lines of a different size and apply to the first set of lines. This creates a bit of variation.
    roll_line *= roll_line * smoothstep(0.3, 0.9, sin(uv.y * roll_size * roll_variation - (rollTime * roll_speed * roll_variation) ) );
    // Distort the UV where where the lines are
    roll_uv = vec2(( roll_line * distort_intensity * (1.-UV.x)), 0.0);
  }

  vec4 text;
  if(roll) {
    // If roll is true distort the texture with roll_uv. The texture is split up into RGB to
    // make some chromatic aberration. We apply the aberration to the red and green channels accorging to the aberration parameter
    // and intensify it a bit in the roll distortion.
    text.r = textureBilinear(screen, text_uv + roll_uv * 0.8 + vec2(aberration, 0.0) * .1).r;
    text.g = textureBilinear(screen, text_uv + roll_uv * 1.2 - vec2(aberration, 0.0) * .1 ).g;
    text.b = texture(screen, text_uv + roll_uv).b;
    text.a = 1.0;
  } else {
    // If roll is false only apply the aberration without any distorion. The aberration values are very small so the .1 is only
    // to make the slider in the Inspector less sensitive.
    text.r = textureBilinear(screen, text_uv + vec2(aberration, 0.0) * .1).r;
    text.g = textureBilinear(screen, text_uv - vec2(aberration, 0.0) * .1).g;
    text.b = texture(screen, text_uv).b;
    text.a = 1.0;
  }

  float r = text.r;
  float g = text.g;
  float b = text.b;

  uv = warp(UV);

  // CRT monitors don't have pixels but groups of red, green and blue dots or lines, called grille. We isolate the texture's color channels
  // and divide it up in 3 offsetted lines to show the red, green and blue colors next to each other, with a small black gap between.
  if(grille_opacity > 0.0) {
    float g_r = smoothstep(0.85, 0.95, abs(sin(uv.x * (resolution.x * 3.14159265))));
    float g_g = smoothstep(0.85, 0.95, abs(sin(1.05 + uv.x * (resolution.x * 3.14159265))));
    float g_b = smoothstep(0.85, 0.95, abs(sin(2.1 + uv.x * (resolution.x * 3.14159265))));

    // Apply the grille in linear color space (gamma-correct) to prevent perceptual brightness shifts
    float r_linear = pow(r, 2.2);
    float g_linear = pow(g, 2.2);
    float b_linear = pow(b, 2.2);

    float r_masked = mix(r_linear, r_linear * g_r, grille_opacity);
    float g_masked = mix(g_linear, g_linear * g_g, grille_opacity);
    float b_masked = mix(b_linear, b_linear * g_b, grille_opacity);

    r = pow(clamp(r_masked, 0.0, 1.0), 1.0 / 2.2);
    g = pow(clamp(g_masked, 0.0, 1.0), 1.0 / 2.2);
    b = pow(clamp(b_masked, 0.0, 1.0), 1.0 / 2.2);
  }

  // Apply the grille to the texture's color channels and apply Brightness. Since the grille and the scanlines (below) make the image very dark you
  // can compensate by increasing the brightness.
  text.r = clamp(r * brightness, 0.0, 1.0);
  text.g = clamp(g * brightness, 0.0, 1.0);
  text.b = clamp(b * brightness, 0.0, 1.0);

  // Scanlines are the horizontal lines that make up the image on a CRT monitor.
  // Here we are actual setting the black gap between each line, which I guess is not the right definition of the word, but you get the idea
  float scanlines = 0.5;
  if(scanlines_opacity > 0.0) {
    // Same technique as above, create lines with sine and applying it to the texture. Smoothstep to allow setting the line size.
    scanlines = smoothstep(scanlines_width, scanlines_width + 0.5, abs(sin(uv.y * (resolution.y * 3.14159265))));
    text.rgb = mix(text.rgb, text.rgb * vec3(scanlines), scanlines_opacity);
  }

  // Apply Bloom (glow bleeds through scanlines)
  if(bloom_intensity > 0.0) {
    vec2 native_res = vec2(textureSize(screen, 0));
    vec2 step = bloom_radius / native_res;
    vec3 blurred = vec3(0.0);
    blurred += sampleHighPass(screen, uv, vec2(-step.x, -step.y), bloom_threshold) * 1.0;
    blurred += sampleHighPass(screen, uv, vec2( 0.0,    -step.y), bloom_threshold) * 2.0;
    blurred += sampleHighPass(screen, uv, vec2( step.x,  -step.y), bloom_threshold) * 1.0;
    blurred += sampleHighPass(screen, uv, vec2(-step.x,  0.0),      bloom_threshold) * 2.0;
    blurred += sampleHighPass(screen, uv, vec2( 0.0,     0.0),      bloom_threshold) * 4.0;
    blurred += sampleHighPass(screen, uv, vec2( step.x,  0.0),      bloom_threshold) * 2.0;
    blurred += sampleHighPass(screen, uv, vec2(-step.x,  step.y), bloom_threshold) * 1.0;
    blurred += sampleHighPass(screen, uv, vec2( 0.0,     step.y), bloom_threshold) * 2.0;
    blurred += sampleHighPass(screen, uv, vec2( step.x,  step.y), bloom_threshold) * 1.0;
    blurred /= 16.0;

    vec3 bloom_color = blurred * 2.0 * bloom_intensity;
    text.rgb = text.rgb + bloom_color - text.rgb * bloom_color;
  }

  // Apply the banded noise.
  if(noise_opacity > 0.0) {
    // Generate a noise pattern that is very stretched horizontally, and animate it with noise_speed
    float noise = smoothstep(0.4, 0.5, noise(uv * vec2(2.0, 200.0) + vec2(10.0, (time * (noise_speed))) ) );

    // We use roll_line (set above) to define how big the noise should be vertically (multiplying cuts off all black parts).
    // We also add in some basic noise with random() to break up the noise pattern above. The noise is sized according to
    // the resolution value set in the inspector. If you don't like this look you can
    // change "ceil(uv * resolution) / resolution" to only "uv" to make it less pixelated. Or multiply resolution with som value
    // greater than 1.0 to make them smaller.
    roll_line *= noise * scanlines * clamp(random((ceil(uv * resolution) / resolution) + vec2(time * 0.8, 0.0)).x + 0.8, 0.0, 1.0);
    // Add it to the texture based on noise_opacity
    text.rgb = clamp(mix(text.rgb, text.rgb + roll_line, noise_opacity), vec3(0.0), vec3(1.0));
  }

  // Apply static noise by generating it over the whole screen in the same way as above
  if(static_noise_intensity > 0.0) {
    text.rgb += clamp(random((ceil(uv * resolution) / resolution) + fract(time)).x, 0.0, 1.0) * static_noise_intensity;
  }

  // Apply a black border to hide imperfections caused by the warping.
  // Also apply the vignette
  text.rgb *= border(uv);
  text.rgb *= vignette(uv);
  // Hides the black border and make that area transparent. Good if you want to add the the texture on top an image of a TV or monitor.
  if(clip_warp) {
    text.a = border(uv);
  }

  // Apply discoloration to get a VHS look (lower saturation and higher contrast)
  // You can play with the values below or expose them in the Inspector.
  float saturation = 0.5;
  float contrast = 1.2;
  if(discolor) {
    // Saturation
    vec3 greyscale = vec3(text.r + text.g + text.b) / 3.;
    text.rgb = mix(text.rgb, greyscale, saturation);

    // Contrast
    float midpoint = pow(0.5, 2.2);
    text.rgb = (text.rgb - vec3(midpoint)) * contrast + midpoint;
  }

  frag = text;
}
