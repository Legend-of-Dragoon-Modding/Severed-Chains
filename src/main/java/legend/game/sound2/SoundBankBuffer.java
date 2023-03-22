package legend.game.sound2;

interface SoundBankBuffer {

  short[] get(final int length);
  void position(final int position);
  int position();
}
