package net.warvale.WorldBorder;

import java.text.DecimalFormat;

public class BorderData
{
    private final double x;
    private final double z;
    private int radiusX;
    private int radiusZ;
    private double maxX;
    private double minX;
    private double maxZ;
    private double minZ;
    
    public BorderData(final double x, final double z, final int radiusX, final int radiusZ) {
        this.radiusX = 0;
        this.radiusZ = 0;
        this.x = x;
        this.z = z;
        this.setRadiusX(radiusX);
        this.setRadiusZ(radiusZ);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public int getRadiusX() {
        return this.radiusX;
    }
    
    public int getRadiusZ() {
        return this.radiusZ;
    }
    
    public void setRadiusX(final int radiusX) {
        this.radiusX = radiusX;
        this.maxX = this.x + radiusX;
        this.minX = this.x - radiusX;
    }
    
    public void setRadiusZ(final int radiusZ) {
        this.radiusZ = radiusZ;
        this.maxZ = this.z + radiusZ;
        this.minZ = this.z - radiusZ;
    }
    
    @Override
    public String toString() {
        return "radius " + ((this.radiusX == this.radiusZ) ? Integer.valueOf(this.radiusX) : (this.radiusX + "x" + this.radiusZ)) + " at X: " + new DecimalFormat("0.0").format(this.x) + " Z: " + new DecimalFormat("0.0").format(this.z);
    }
    
    public boolean insideBorder(final double xLoc, final double zLoc) {
        return xLoc >= this.minX && xLoc <= this.maxX && zLoc >= this.minZ && zLoc <= this.maxZ;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final BorderData test = (BorderData)obj;
        return test.x == this.x && test.z == this.z && test.radiusX == this.radiusX && test.radiusZ == this.radiusZ;
    }
    
    @Override
    public int hashCode() {
        return ((int)(this.x * 10.0) << 4) + (int)this.z + (this.radiusX << 2) + (this.radiusZ << 3);
    }
}
