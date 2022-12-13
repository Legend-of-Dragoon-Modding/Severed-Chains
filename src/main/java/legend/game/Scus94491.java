package legend.game;

import legend.core.Hardware;
import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static legend.core.Hardware.MEMORY;

public final class Scus94491 {
  private Scus94491() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491.class);

  public static final Value _80010000 = MEMORY.ref(4, 0x80010000L);

  public static final Value bpe_80188a88 = MEMORY.ref(4, 0x80188a88L);

  @Method(0x801bf2a0L)
  public static byte[] decompress(final byte[] archive) {
    // Check BPE header - this check is in the BPE block method but not the main EXE method
    if(MathHelper.get(archive, 4, 4) != 0x1a455042L) {
      throw new RuntimeException("Attempted to decompress non-BPE segment");
    }

    LOGGER.info("Decompressing BPE segment");

    final byte[] dest = new byte[(int)MathHelper.get(archive, 0, 4)];

    final Deque<Byte> unresolved_byte_list = new LinkedList<>();
    final byte[] dict_leftch = new byte[0x100];
    final byte[] dict_rightch = new byte[0x100];

    int totalSize = 0;
    int archiveOffset = 8;
    int destinationOffset = 0;

    // Each block is preceded by 4-byte int up to 0x800 giving the number
    // of decompressed bytes in the block. 0x00000000 indicates that there
    // are no further blocks and decompression is complete.
    int bytes_remaining_in_block = (int)MathHelper.get(archive, archiveOffset, 4);
    archiveOffset += 4;

    while(bytes_remaining_in_block != 0) {
      if(bytes_remaining_in_block > 0x800) {
        LOGGER.error("Decompress: 0x%08x at offset 0x%08x is an invalid block size", bytes_remaining_in_block, archiveOffset - 4);
        throw new RuntimeException("Decompression error");
      }

      totalSize += bytes_remaining_in_block;

      // Build the initial dictionary/lookup table. The left-character dict
      // is filled so that each key contains itself as a value, while the
      // right-character dict is filled with empty values.
      Arrays.fill(dict_rightch, (byte)0);
      for(int i = 0; i < 0x100; i++) {
        dict_leftch[i] = (byte)i;
      }

      // Build adaptive dictionary.
      int key = 0;
      while(key < 0x100) {
        // Dictionary is 256 bytes long. Loop until all keys filled.
        // If byte_pairs_to_read is >= 0x80, then only the next byte will
        // be read into the dictionary, placed at the index value calculated
        // using the below formula. Otherwise, the byte indicates how many
        // sequential bytes to read into the dictionary.
        int byte_pairs_to_read = archive[archiveOffset] & 0xff;
        archiveOffset++;

        if(byte_pairs_to_read >= 0x80) {
          key = key - 0x7f + byte_pairs_to_read;
          byte_pairs_to_read = 0;
        }

        // For each byte/byte pair to read, read the next byte and add it
        // to the leftch dict at the current key. If the character matches
        // the key it's at, increment key and continue. If it does not,
        // read the next character and add it to the same key in the
        // rightch dict before incrementing key and continuing.
        if(key < 0x100) {
          // Check that dictionary length not exceeded.
          for(int i = 0; i < byte_pairs_to_read + 1; i++) {
            dict_leftch[key] = archive[archiveOffset];
            archiveOffset++;

            if((dict_leftch[key] & 0xff) != key) {
              dict_rightch[key] = archive[archiveOffset];
              archiveOffset++;
            }

            key++;
          }
        }
      }

      // Decompress block
      // On each pass, read one byte and add it to a list of unresolved bytes.
      while(bytes_remaining_in_block > 0) {
        unresolved_byte_list.clear();
        unresolved_byte_list.push(archive[archiveOffset]);
        archiveOffset++;

        // Pop the first item in the list of unresolved bytes. If the
        // byte key == value in dict_leftch, append it to the list of
        // decompressed bytes. If the byte key !=value in dict_leftch,
        // insert the leftch followed by rightch to the unresolved byte
        // list. Loop until the unresolved byte list is empty.
        while(!unresolved_byte_list.isEmpty()) {
          final byte compressed_byte = unresolved_byte_list.pop();
          if(compressed_byte == dict_leftch[compressed_byte & 0xff]) {
            dest[destinationOffset] = compressed_byte;
            destinationOffset++;
            bytes_remaining_in_block--;
          } else {
            unresolved_byte_list.push(dict_rightch[compressed_byte & 0xff]);
            unresolved_byte_list.push(dict_leftch[compressed_byte & 0xff]);
          }
        }
      }

      if(archiveOffset % 4 != 0) {
        // Word - align the pointer.
        archiveOffset = archiveOffset + 4 - archiveOffset % 4;
      }

      bytes_remaining_in_block = (int)MathHelper.get(archive, archiveOffset, 4);
      archiveOffset += 4;
    }

    LOGGER.info("Archive size: %d, decompressed size: %d", archiveOffset, totalSize);

    return dest;
  }

  @Method(0x801bf460L)
  public static void main() {
    LOGGER.info("--- SCUS 94491 start! ---");

    final byte[] archive = MEMORY.getBytes(bpe_80188a88.getAddress(), 221736);
    final byte[] decompressed = Scus94491.decompress(archive);
    MEMORY.setBytes(_80010000.getAddress(), decompressed);

    MEMORY.addFunctions(Scus94491BpeSegment.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8002.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8003.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8004.class);
    MEMORY.addFunctions(Scus94491BpeSegment_800e.class);
    Scus94491BpeSegment_8002.start();

    assert !Hardware.isAlive() : "Shouldn't get here";
  }
}
