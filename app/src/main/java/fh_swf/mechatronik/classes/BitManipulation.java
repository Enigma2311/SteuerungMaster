package fh_swf.mechatronik.classes;

/**
 * Hilfsklasse für die BitManipulationen.
 *
 * Created by zero_ on 19.07.2018.
 */
public class BitManipulation {

    /**
     * Methode die es ermöglicht ein bestimmtes Bit in einem übergebenem Byte zu setzen.
     *
     * @param n   Das Byte in welchem ein Bit gesetzt werden soll.
     * @param pos Die Position des Bits welches gesetzt werden soll.
     * @return Das Byte mit dem gesetzten Bit.
     */

    public static byte setBit(byte n, int pos) {
        return (byte) (n | (1 << pos));
    }

    /**
     * Methode die es ermöglicht ein gestztes Bit in einem übergeben Byte wieder zu löschen.
     *
     * @param n   Das Byte in welchem ein gesetztes Bit gelöscht werden soll.
     * @param pos Die Position des Bits, welches gelöscht werden soll.
     * @return Das Byte mit dem zurückgesetzten Bit.
     */

    public static byte clearBit(byte n, int pos) {
        return (byte) (n & ~(1 << pos));
    }

}
