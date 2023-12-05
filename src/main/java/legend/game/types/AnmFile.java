package legend.game.types;

import legend.game.unpacker.FileData;

public class AnmFile {
  public final int id_00;
  public final int version_01;
  public final int flag_02;
  /** Number of sprite groups */
  public final int n_sprite_gp_04;
  /** Number of sequences */
  public final int n_sequence_06;
  public final AnmSequence[] sequences_08;
  public final AnmSpriteGroup[] spriteGroups;

  public AnmFile(final FileData data) {
    this.id_00 = data.readUByte(0x0);
    this.version_01 = data.readUByte(0x1);
    this.flag_02 = data.readUShort(0x2);
    this.n_sprite_gp_04 = data.readUShort(0x4);
    this.n_sequence_06 = data.readUShort(0x6);
    this.sequences_08 = new AnmSequence[this.n_sequence_06];
    this.spriteGroups = new AnmSpriteGroup[this.n_sprite_gp_04];

    for(int i = 0; i < this.n_sequence_06; i++) {
      this.sequences_08[i] = new AnmSequence(data.slice(0x8 + i * 0x8, 0x8));
    }

    for(int i = 0; i < this.n_sprite_gp_04; i++) {
      this.spriteGroups[i] = new AnmSpriteGroup(data.slice(data.readInt(0x8 + this.n_sequence_06 * 0x8 + i * 0x4)));
    }
  }
}
