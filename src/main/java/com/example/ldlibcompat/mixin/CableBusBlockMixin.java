package your.mod.mixin.ae2;

import appeng.block.networking.CableBusBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mixin(CableBusBlock.class)
public abstract class CableBusBlockMixin {

    private static final Logger LOGGER =
            LogManager.getLogger("AE2AsyncModelFix");

    private static final AtomicBoolean LOGGED = new AtomicBoolean(false);

	@Redirect(
		method = "getAppearance",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraftforge/client/model/data/ModelDataManager;getAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraftforge/client/model/data/ModelData;"
		)
	)
	private ModelData ae2asyncfix$safeGetModelData(
			ModelDataManager manager,
			BlockPos pos
	) {
		try {
			if (LOGGED.compareAndSet(false, true)) {
				LOGGER.warn(
					"AE2AsyncModelFix ACTIVE: CableBusBlock.getAppearance on thread '{}'",
					Thread.currentThread().getName()
				);
			}
			return manager.getAt(pos);
		} catch (ConcurrentModificationException e) {
			LOGGER.warn(
				"AE2AsyncModelFix: CME caught at {} (thread {})",
				pos,
				Thread.currentThread().getName()
			);
			return ModelData.EMPTY;
		}
	}

}
