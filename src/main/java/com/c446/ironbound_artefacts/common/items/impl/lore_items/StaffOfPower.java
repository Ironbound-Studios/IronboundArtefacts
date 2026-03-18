package com.c446.ironbound_artefacts.common.items.impl.lore_items;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;

//import static com.c446.ironbound_artefacts.registries.ItemRegistry.STAFF_OF_POWER;

public class StaffOfPower extends StaffItem {
    public StaffOfPower(Properties properties) {
        super(properties);
    }

/*
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> lines, @NotNull TooltipFlag tooltipFlag) {
        AtomicReference<String> spellName = new AtomicReference<>("");
            if (stack != null && stack.is(STAFF_OF_POWER)) {
                ISpellContainer mainHandSpellContainer = ISpellContainer.get(stack);
                if (mainHandSpellContainer != null && mainHandSpellContainer.getAllSpells() != null) {
                    Arrays.stream(mainHandSpellContainer.getAllSpells()).forEach(spell -> {
                        if (spell != null && spell.getSpell() != null) {
                            spellName.set(spell.getSpell().getSpellName());
                            lines.add(Component.empty());
                            lines.add(Component.translatable("item.modifiers.hand").withStyle(ChatFormatting.GOLD));
                            MutableComponent name = spell.getSpell().getDisplayName(MinecraftInstanceHelper.instance.player()).withStyle(spell.getSpell().getSchoolType().getDisplayName().getStyle());
                            lines.add(Component.literal(" ").append(
                                    (Component.translatable("tooltip.irons_spellbooks.enhance_spell_level_plural", Component.literal("1"), name))
                                            .withStyle(ChatFormatting.YELLOW)));

                        }
                    });
                }

        }



        super.appendHoverText(stack, context, lines, tooltipFlag);
    }*/
}
