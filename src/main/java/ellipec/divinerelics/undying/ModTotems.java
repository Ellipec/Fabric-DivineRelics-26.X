package ellipec.divinerelics.undying;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;

import java.util.List;

public class ModTotems {
    public static final DeathProtection HEARTOFAEGIS = new DeathProtection(List.of(new ClearAllStatusEffectsConsumeEffect(),
            new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION,900,3),
                    new MobEffectInstance(MobEffects.ABSORPTION, 600, 4), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0),
                    new MobEffectInstance(MobEffects.RESISTANCE, 1200, 1)))));

}
