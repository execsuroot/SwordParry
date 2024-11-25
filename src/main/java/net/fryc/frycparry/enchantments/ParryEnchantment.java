package net.fryc.frycparry.enchantments;

import net.fryc.frycparry.tag.ModItemTags;
import net.fryc.frycparry.util.ConfigHelper;
import net.fryc.frycparry.util.ParryHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

public class ParryEnchantment extends Enchantment {
    protected ParryEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    public int getMaxLevel() {
        return 2;
    }

    public int getMinPower(int level) {
        return 5 + 20 * (level - 1);
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ShieldItem || (!ParryHelper.isItemParryDisabledWithConfig(stack) && !stack.isIn(ModItemTags.PARRYING_EXCLUDED_ITEMS));
    }

    public boolean isTreasure() {
        return !ConfigHelper.enableParryEnchantment;
    }

    public boolean isAvailableForEnchantedBookOffer() {
        return ConfigHelper.enableParryEnchantment;
    }

    public boolean isAvailableForRandomSelection() {
        return ConfigHelper.enableParryEnchantment;
    }
}
