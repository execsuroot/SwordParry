package net.fryc.frycparry.mixin;

import net.fryc.frycparry.FrycParry;
import net.fryc.frycparry.effects.ModEffects;
import net.fryc.frycparry.util.CanBlock;
import net.fryc.frycparry.util.ParryHelper;
import net.fryc.frycparry.util.ParryItem;
import net.fryc.frycparry.util.ServerParryKeyUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }



    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V", shift = At.Shift.AFTER))
    private void setBlockCooldownOnItemSwap(CallbackInfo info) {
        PlayerEntity dys = ((PlayerEntity)(Object)this);
        Item item = dys.getMainHandStack().getItem();
        if(dys.isUsingItem()) dys.stopUsingItem();

        //onStoppedUsingParry() is not used when player switches item while blocking
        ((CanBlock) dys).setBlockingDataToFalse();
        ((CanBlock) dys).setParryDataToFalse();
        if(dys instanceof ServerPlayerEntity sPlayer){
            ((ServerParryKeyUser) sPlayer).changePressedParryKeyValueToFalse();
        }//todo naprawic cooldown przy switchowaniu itemow

        if(ParryHelper.canParry(dys) && !ParryHelper.isItemParryDisabled(dys.getMainHandStack().getItem())){
            if(!dys.getItemCooldownManager().isCoolingDown(dys.getMainHandStack().getItem())) dys.getItemCooldownManager().set(dys.getMainHandStack().getItem(), ((ParryItem) dys.getMainHandStack().getItem()).getCooldownAfterInterruptingBlockAction());
        }
        else {
            if(dys.getMainHandStack().getItem() instanceof ShieldItem){
                if(!dys.getItemCooldownManager().isCoolingDown(dys.getMainHandStack().getItem())) dys.getItemCooldownManager().set(dys.getMainHandStack().getItem(), FrycParry.config.cooldownAfterInterruptingShieldBlockAction);
            }
            else if(dys.getOffHandStack().getItem() instanceof ShieldItem) {
                if(!dys.getItemCooldownManager().isCoolingDown(dys.getOffHandStack().getItem())) dys.getItemCooldownManager().set(dys.getOffHandStack().getItem(), FrycParry.config.cooldownAfterInterruptingShieldBlockAction);
            }
        }
    }



    @Inject(method = "tick()V", at = @At("TAIL"))
    private void disarm(CallbackInfo info) {
        PlayerEntity dys = ((PlayerEntity)(Object)this);
        if(dys.hasStatusEffect(ModEffects.DISARMED)){
            --this.lastAttackedTicks;
        }
    }

    @Inject(method = "resetLastAttackedTicks()V", at = @At("HEAD"))
    private void setCooldownForParry(CallbackInfo info) {
        PlayerEntity dys = ((PlayerEntity)(Object)this);
        if(this.lastAttackedTicks > 10){
            if(ParryHelper.canParry(dys)){
                if(!dys.getItemCooldownManager().isCoolingDown(dys.getMainHandStack().getItem())) dys.getItemCooldownManager().set(dys.getMainHandStack().getItem(), 10);
            }
            else if(ParryHelper.hasShieldEquipped(dys)){
                if(dys.getMainHandStack().getItem() instanceof ShieldItem){
                    if(!dys.getItemCooldownManager().isCoolingDown(dys.getMainHandStack().getItem())) dys.getItemCooldownManager().set(dys.getMainHandStack().getItem(), 10);
                }
                else {
                    if(!dys.getItemCooldownManager().isCoolingDown(dys.getOffHandStack().getItem())) dys.getItemCooldownManager().set(dys.getOffHandStack().getItem(), 10);
                }
            }
        }

    }


    @Redirect(method = "Lnet/minecraft/entity/player/PlayerEntity;attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isAttackable()Z"))
    private boolean unAttackable(Entity target) {
        PlayerEntity dys = ((PlayerEntity)(Object)this);
        return target.isAttackable() && !dys.hasStatusEffect(ModEffects.DISARMED);
    }

    @Redirect(method = "Lnet/minecraft/entity/player/PlayerEntity;takeShieldHit(Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;disablesShield()Z"))
    private boolean dontDisableShield(LivingEntity attacker) {
        return false;
    }


}
