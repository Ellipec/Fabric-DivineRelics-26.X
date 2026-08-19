package ellipec.divinerelics.item.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;

public class HeartofAegis {
    public record DeathProtection(List<ConsumeEffect> deathEffects) {
        public static final Codec<net.minecraft.world.item.component.DeathProtection> CODEC = RecordCodecBuilder.create((i) -> i.group(ConsumeEffect.CODEC.listOf().optionalFieldOf("death_effects", List.of()).forGetter(net.minecraft.world.item.component.DeathProtection::deathEffects)).apply(i, net.minecraft.world.item.component.DeathProtection::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, DeathProtection> STREAM_CODEC;
    public static final DeathProtection HEART_OF_AEGIS;


    public void applyEffects(final ItemStack itemStack, final LivingEntity entity) {
        for(ConsumeEffect effect : this.deathEffects) {
            effect.apply(entity.level(), itemStack, entity);
        }

    }

    static {
        STREAM_CODEC = StreamCodec.composite(ConsumeEffect.STREAM_CODEC.apply(ByteBufCodecs.list()), DeathProtection::deathEffects, DeathProtection::new);
        HEART_OF_AEGIS = new DeathProtection(List.of(new ClearAllStatusEffectsConsumeEffect(),
                new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(MobEffects.REGENERATION, 900, 3),
                        new MobEffectInstance(MobEffects.ABSORPTION, 600, 4), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0),
                        new MobEffectInstance(MobEffects.RESISTANCE, 1200, 4)))));
    }
}


}
