package net.warvale.WorldBorder;

public class CoordXZ
{
    public int x;
    public int z;
    
    public CoordXZ(final int x, final int z) {
        this.x = x;
        this.z = z;
    }
    
    public static int blockToChunk(final int blockVal) {
        return blockVal >> 4;
    }
    
    public static int chunkToRegion(final int chunkVal) {
        return chunkVal >> 5;
    }
    
    public static int chunkToBlock(final int chunkVal) {
        return chunkVal << 4;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final CoordXZ test = (CoordXZ)obj;
        return test.x == this.x && test.z == this.z;
    }
    
    @Override
    public int hashCode() {
        return (this.x << 9) + this.z;
    }
}
