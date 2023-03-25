package legend.game.sound2;

/*from  w w  w .  j av  a  2s . co  m
Copyright 2009 The Open University
  http://www.open.ac.uk/lts/projects/audioapplets/

  This file is part of the "Open University audio applets" project.

  The "Open University audio applets" project is free software: you can
  redistribute it and/or modify it under the terms of the GNU General Public
  License as published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  The "Open University audio applets" project is distributed in the hope that it
  will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with the "Open University audio applets" project.
  If not, see <http://www.gnu.org/licenses/>.
  */

class Resampler {
  /**
   * Resamples audio data using simple methods.
   * @param data Input data
   * @param length Amount of input buffer that is actually used
   * @param stereo True if input is stereo
   * @param inFrequency Frequency of input
   * @param outFrequency Frequency of output
   * @return Resampled audio data
   */
  public static short[] resample(short[] data, int length,
                                 boolean stereo, int inFrequency, int outFrequency) {
    if (inFrequency < outFrequency) {
      return upsample(data, length, stereo, inFrequency, outFrequency);
    }
    if (inFrequency > outFrequency) {
      return downsample(data, length, stereo, inFrequency,
        outFrequency);
    }
    return trimArray(data, length);
  }

  /**
   * Basic upsampling algorithm. Uses a linear approximation to fill in the
   * missing data.
   * @param data Input data
   * @param length Amount of input buffer that is actually used
   * @param stereo True if input is stereo
   * @param inFrequency Frequency of input
   * @param outFrequency Frequency of output
   * @return Upsampled audio data
   */
  private static short[] upsample(short[] data, int length,
                                  boolean stereo, int inFrequency, int outFrequency) {
    // Special case for no action
    if (inFrequency == outFrequency) {
      return trimArray(data, length);
    }

    double scale = (double) inFrequency / (double) outFrequency;
    double pos = 0.0;
    short[] output;
    if (!stereo) {
      output = new short[(int) (length / scale)];
      for (int i = 0; i < output.length; i++) {
        int inPos = (int) pos;
        double proportion = pos - inPos;
        if (inPos >= length - 1) {
          inPos = length - 2;
          proportion = 1.0;
        }

        output[i] = (short) Math
          .round(data[inPos] * (1.0 - proportion)
            + data[inPos + 1] * proportion);
        pos += scale;
      }
    } else {
      output = new short[2 * (int) ((length / 2) / scale)];
      for (int i = 0; i < output.length / 2; i++) {
        int inPos = (int) pos;
        double proportion = pos - inPos;

        int inRealPos = inPos * 2;
        if (inRealPos >= length - 3) {
          inRealPos = length - 4;
          proportion = 1.0;
        }

        output[i * 2] = (short) Math.round(data[inRealPos]
          * (1.0 - proportion) + data[inRealPos + 2]
          * proportion);
        output[i * 2 + 1] = (short) Math.round(data[inRealPos + 1]
          * (1.0 - proportion) + data[inRealPos + 3]
          * proportion);
        pos += scale;
      }
    }

    return output;
  }

  /**
   * Basic downsampling algorithm. Uses linear approximation to reduce data.
   * @param data Input data
   * @param length Amount of input buffer that is actually used
   * @param stereo True if input is stereo
   * @param inFrequency Frequency of input
   * @param outFrequency Frequency of output
   * @return Downsampled audio data
   */
  private static short[] downsample(short[] data, int length,
                                    boolean stereo, int inFrequency, int outFrequency) {
    // Special case for no action
    if (inFrequency == outFrequency) {
      return trimArray(data, length);
    }

    double scale = (double) outFrequency / (double) inFrequency;
    short[] output;
    double pos = 0.0;
    int outPos = 0;
    if (!stereo) {
      double sum = 0.0;
      output = new short[(int) (length * scale)];
      int inPos = 0;
      while (outPos < output.length) {
        double thisVal = (double) data[inPos++];
        double nextPos = pos + scale;
        if (nextPos >= 1.0) {
          sum += thisVal * (1.0 - pos);
          output[outPos++] = (short) Math.round(sum);
          nextPos -= 1.0;
          sum = nextPos * thisVal;
        } else {
          sum += scale * thisVal;
        }
        pos = nextPos;

        if (inPos >= length && outPos < output.length) {
          output[outPos++] = (short) Math.round(sum / pos);
        }
      }
    } else {
      double sum1 = 0.0, sum2 = 0.0;
      output = new short[2 * (int) ((length / 2) * scale)];
      int inPos = 0;
      while (outPos < output.length) {
        double thisVal1 = (double) data[inPos++], thisVal2 = (double) data[inPos++];
        double nextPos = pos + scale;
        if (nextPos >= 1.0) {
          sum1 += thisVal1 * (1.0 - pos);
          sum2 += thisVal2 * (1.0 - pos);
          output[outPos++] = (short) Math.round(sum1);
          output[outPos++] = (short) Math.round(sum2);
          nextPos -= 1.0;
          sum1 = nextPos * thisVal1;
          sum2 = nextPos * thisVal2;
        } else {
          sum1 += scale * thisVal1;
          sum2 += scale * thisVal2;
        }
        pos = nextPos;

        if (inPos >= length && outPos < output.length) {
          output[outPos++] = (short) Math.round(sum1 / pos);
          output[outPos++] = (short) Math.round(sum2 / pos);
        }
      }
    }

    return output;
  }

  /**
   * @param data Data
   * @param length Length of valid data
   * @return Array trimmed to length (or same array if it already is)
   */
  public static short[] trimArray(short[] data, int length) {
    if (data.length == length) {
      return data;
    } else {
      short[] output = new short[length];
      System.arraycopy(output, 0, data, 0, length);
      return output;
    }
  }

  /**
   * @param data Data
   * @param length Length of valid data
   * @return Array trimmed to length (or same array if it already is)
   */
  public static byte[] trimArray(byte[] data, int length) {
    if (data.length == length) {
      return data;
    } else {
      byte[] output = new byte[length];
      System.arraycopy(output, 0, data, 0, length);
      return output;
    }
  }
}
