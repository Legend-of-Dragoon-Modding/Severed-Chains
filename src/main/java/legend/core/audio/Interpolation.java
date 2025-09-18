package legend.core.audio;

import legend.core.audio.sequencer.LookupTables;

public enum Interpolation {
  Four(4) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.catmullRomSplines(interpolationStep);
    }
  },
  Six(6) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.lagrangeWeights(interpolationStep, this.taps);
    }
  },
  Eight(8) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.lagrangeWeights(interpolationStep, this.taps);
    }
  },
  Ten(10) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.lagrangeWeights(interpolationStep, this.taps);
    }
  },
  Twelve(12) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.lagrangeWeights(interpolationStep, this.taps);
    }
  },
  Fourteen(14) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.lagrangeWeights(interpolationStep, this.taps);
    }
  },
  Sixteen(16) {
    @Override
    public float[][] generateWeights(final int interpolationStep) {
      return LookupTables.lagrangeWeights(interpolationStep, this.taps);
    }
  };

  public final int taps;

  Interpolation(final int taps) {
    this.taps = taps;
  }

  public abstract float[][] generateWeights(int interpolationStep);
}
