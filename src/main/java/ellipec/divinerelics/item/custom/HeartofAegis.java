package ellipec.divinerelics.item.custom;


import com.mojang.serialization.Codec;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.advancements.criterion.UsedTotemTrigger;


public class HeartofAegis extends SimpleCriterionTrigger<UsedTotemTrigger.TriggerInstance> {
    public void UsedTotemTrigger() {

    }

    @Override
    public Codec<UsedTotemTrigger.TriggerInstance> codec() { return  UsedTotemTrigger.TriggerInstance.CODEC;}
    }

