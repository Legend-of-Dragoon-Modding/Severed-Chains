package legend.core.audio.opus;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment Header as defined by <a href="https://datatracker.ietf.org/doc/html/rfc7845#section-5.2">RFC7845</a>
 * <table>
 *  <tr><th></th><th style="text-align: center">0</th><th></th><th style="text-align: center">1</th><th></th><th style="text-align: center">2</th><th></th><th style="text-align: center">3</th></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">O</td><td style="text-align: center">|</td><td style="text-align: center">p</td><td style="text-align: center">|</td><td style="text-align: center">u</td><td style="text-align: center">|</td><td style="text-align: center">s</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">T</td><td style="text-align: center">|</td><td style="text-align: center">a</td><td style="text-align: center">|</td><td style="text-align: center">g</td><td style="text-align: center">|</td><td style="text-align: center">s</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>Vendor String Length</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>{@link OpusTags#vendorString}</td><td style="text-align: center">:</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>User Comment List Length</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>User Comment #0 String Length</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>User Comment #0 String</td><td style="text-align: center">:</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">:</td><td style="text-align: center" colspan=7></td><td style="text-align: center">:</td></tr>
 * </table>
 */
final class OpusTags {
  private final static byte[] MAGIC_SIGNATURE = {'O', 'p', 'u', 's', 'T', 'a', 'g', 's'};
  private final String vendorString = "Severed Chains";
  private final List<String> comments = new ArrayList<>();

  OpusTags() {
    this.comments.add("encoder=libopus 0, opus 0");
  }

  void addComment(final String comment) {
    this.comments.add(comment);
  }

  byte[] toBytes() {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    bos.writeBytes(MAGIC_SIGNATURE);
    bos.write(this.vendorString.length());
    bos.write(this.vendorString.length() >>> 8);
    bos.write(this.vendorString.length() >>> 16);
    bos.write(this.vendorString.length() >>> 24);
    bos.writeBytes(this.vendorString.getBytes(StandardCharsets.UTF_8));

    bos.write(this.comments.size());
    bos.write(this.comments.size() >>> 8);
    bos.write(this.comments.size() >>> 16);
    bos.write(this.comments.size() >>> 24);
    for(final String comment : this.comments) {
      bos.write(comment.length());
      bos.write(comment.length() >>> 8);
      bos.write(comment.length() >>> 16);
      bos.write(comment.length() >>> 24);
      bos.writeBytes(comment.getBytes(StandardCharsets.UTF_8));
    }

    return bos.toByteArray();
  }
}
