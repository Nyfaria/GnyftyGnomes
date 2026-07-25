package com.nyfaria.gnyftygnomes.mixin;

import com.mojang.datafixers.util.Either;
import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.worldgen.GnomeStructureProcessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public class MixinSinglePoolElement {
    @Shadow
    @Final
    protected Either<ResourceLocation, StructureTemplate> template;

    @Unique
    private static boolean gnyftygnomes$errorLogged = false;

    @Inject(method = "getSettings", at = @At("RETURN"))
    private void gnyftygnomes$addGnomeProcessor(Rotation rotation, BoundingBox box, boolean offset, CallbackInfoReturnable<StructurePlaceSettings> cir) {
        try {
            StructurePlaceSettings settings = cir.getReturnValue();
            if (settings == null || this.template == null) {
                return;
            }
            this.template.left().ifPresent(location -> {
                if (location.getNamespace().equals("minecraft") && location.getPath().startsWith("village/")) {
                    settings.addProcessor(GnomeStructureProcessor.INSTANCE);
                }
            });
        } catch (Throwable t) {
            if (!gnyftygnomes$errorLogged) {
                gnyftygnomes$errorLogged = true;
                Constants.LOG.error("Failed to add gnome structure processor", t);
            }
        }
    }
}
