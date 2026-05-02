package com.zeutd.propulsed.content;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PropBlockStateProperties {
    public static final EnumProperty<Surrounding> SURROUNDING = EnumProperty.create("surrounding", Surrounding.class);
    public static final BooleanProperty CAN_INPUT = BooleanProperty.create("can_input");
}
