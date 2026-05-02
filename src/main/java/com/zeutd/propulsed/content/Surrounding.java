package com.zeutd.propulsed.content;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Comparator;

public enum Surrounding implements StringRepresentable {
    EAST_SOUTH_UP      (0 , 25, -1, "east_south_up",       new Vec3i(1 , 1 , 1)),
    EAST_MIDDLE_UP     (1 , 24, -1, "east_middle_up",      new Vec3i(1 , 1 , 0)),
    EAST_NORTH_UP      (2 , 23, -1, "east_north_up",       new Vec3i(1 , 1 ,-1)),
    MIDDLE_SOUTH_UP    (3 , 22, -1, "middle_south_up",     new Vec3i(0 , 1 , 1)),
    MIDDLE_MIDDLE_UP   (4 , 21, -1, "middle_middle_up",    new Vec3i(0 , 1 , 0)),
    MIDDLE_NORTH_UP    (5 , 20, -1, "middle_north_up",     new Vec3i(0 , 1 ,-1)),
    WEST_SOUTH_UP      (6 , 19, -1, "west_south_up",       new Vec3i(-1, 1 , 1)),
    WEST_MIDDLE_UP     (7 , 18, -1, "west_middle_up",      new Vec3i(-1, 1 , 0)),
    WEST_NORTH_UP      (8 , 17, -1, "west_north_up",       new Vec3i(-1, 1 ,-1)),
    EAST_SOUTH_MIDDLE  (9 , 16, 0 , "east_south_middle",   new Vec3i(1 , 0 , 1)),
    EAST_MIDDLE_MIDDLE (10, 15, 1 , "east_middle_middle",  new Vec3i(1 , 0 , 0)),
    EAST_NORTH_MIDDLE  (11, 14, 2 , "east_north_middle",   new Vec3i(1 , 0 ,-1)),
    MIDDLE_SOUTH_MIDDLE(12, 13, 7 , "middle_south_middle", new Vec3i(0 , 0 , 1)),
    //MIDDLE_MIDDLE_MIDDLE skipped
    MIDDLE_NORTH_MIDDLE(13, 12, 3 , "middle_north_middle", new Vec3i(0 , 0 ,-1)),
    WEST_SOUTH_MIDDLE  (14, 11, 6 , "west_south_middle",   new Vec3i(-1, 0 , 1)),
    WEST_MIDDLE_MIDDLE (15, 10, 5 , "west_middle_middle",  new Vec3i(-1, 0 , 0)),
    WEST_NORTH_MIDDLE  (16, 9 , 4 , "west_north_middle",   new Vec3i(-1, 0 ,-1)),
    EAST_SOUTH_DOWN    (17, 8 , -1, "east_south_down",     new Vec3i(1 ,-1 , 1)),
    EAST_MIDDLE_DOWN   (18, 7 , -1, "east_middle_down",    new Vec3i(1 ,-1 , 0)),
    EAST_NORTH_DOWN    (19, 6 , -1, "east_north_down",     new Vec3i(1 ,-1 ,-1)),
    MIDDLE_SOUTH_DOWN  (20, 5 , -1, "middle_south_down",   new Vec3i(0 ,-1 , 1)),
    MIDDLE_MIDDLE_DOWN (21, 4 , -1, "middle_middle_down",  new Vec3i(0 ,-1 , 0)),
    MIDDLE_NORTH_DOWN  (22, 3 , -1, "middle_north_down",   new Vec3i(0 ,-1 ,-1)),
    WEST_SOUTH_DOWN    (23, 2 , -1, "west_south_down",     new Vec3i(-1,-1 , 1)),
    WEST_MIDDLE_DOWN   (24, 1 , -1, "west_middle_down",    new Vec3i(-1,-1 , 0)),
    WEST_NORTH_DOWN    (25, 0 , -1, "west_north_down",     new Vec3i(-1,-1 ,-1)),
    ;

    public final int data3d;
    public final int oppositeIndex;
    public final int data2d;
    public final String name;
    private final Vec3i relativePos;

    private static final Surrounding[] BY_3D_DATA = Arrays.stream(values()).sorted(Comparator.comparingInt(s -> s.data3d)).toArray(Surrounding[]::new);
    private static final Surrounding[] BY_2D_DATA = Arrays.stream(values()).filter(s -> s.relativePos.getY() == 0).sorted(Comparator.comparingInt(s -> s.data3d)).toArray(Surrounding[]::new);

    public static Surrounding from3DDataValue(int index) {
        return BY_3D_DATA[Mth.abs(index % BY_3D_DATA.length)];
    }

    public static Surrounding from2DDataValue(int horizontalIndex) {
        return BY_2D_DATA[Mth.abs(horizontalIndex % BY_2D_DATA.length)];
    }

    public int getStepX() {
        return this.relativePos.getX();
    }

    public int getStepY() {
        return this.relativePos.getY();
    }

    public int getStepZ() {
        return this.relativePos.getZ();
    }

    public Vector3f step() {
        return new Vector3f((float)this.getStepX(), (float)this.getStepY(), (float)this.getStepZ());
    }

    public Vec3i getRelativePos() {
        return relativePos;
    }

    public String getName() {
        return this.name;
    }

    public Surrounding getOpposite() {
        return from3DDataValue(this.oppositeIndex);
    }

    Surrounding(int data3d, int oppositeIndex, int data2d, String name, Vec3i relativePos) {
        this.data3d = data3d;
        this.data2d = data2d;
        this.oppositeIndex = oppositeIndex;
        this.name = name;
        this.relativePos = relativePos;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
