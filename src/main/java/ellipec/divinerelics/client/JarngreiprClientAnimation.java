
package ellipec.divinerelics.client;

public class JarngreiprClientAnimation {

    public enum State {
        IDLE,
        CHARGING,
        SWINGING,
        RECOVERY
    }

    private static State state = State.IDLE;

    private static int ticksRemaining = 0;
    private static int animationTicks = 0;
    private static float chargeProgress = 0.0f;

    public static final int ANIMATION_LENGTH = 15;
    public static final int SWING_LENGTH = 7;
    public static final int RECOVERY_LENGTH = 5;

    // Existing animation support
    public static void start() {
        ticksRemaining = ANIMATION_LENGTH;
    }

    public static void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }

        if (state == State.SWINGING) {
            animationTicks++;

            if (animationTicks >= SWING_LENGTH) {
                state = State.RECOVERY;
                animationTicks = 0;
            }
        } else if (state == State.RECOVERY) {
            animationTicks++;

            if (animationTicks >= RECOVERY_LENGTH) {
                stop();
            }
        }
    }

    public static boolean isPlaying() {
        return ticksRemaining > 0
                || state == State.SWINGING
                || state == State.RECOVERY;
    }

    public static int getTicksRemaining() {
        return ticksRemaining;
    }

    public static int getElapsedTicks() {
        return ANIMATION_LENGTH - ticksRemaining;
    }

    // Brutal Swing animation
    public static void startCharging() {
        state = State.CHARGING;
        animationTicks = 0;
        chargeProgress = 0.0f;
    }

    public static void updateCharge(float progress) {
        if (state == State.CHARGING) {
            chargeProgress = Math.clamp(progress, 0.0f, 1.0f);
        }
    }

    public static void startSwing() {
        state = State.SWINGING;
        animationTicks = 0;
        ticksRemaining = 0;
    }

    public static void stop() {
        state = State.IDLE;
        animationTicks = 0;
        ticksRemaining = 0;
        chargeProgress = 0.0f;
    }

    public static State getState() {
        return state;
    }

    public static float getChargeProgress() {
        return chargeProgress;
    }

    public static float getSwingProgress(float tickDelta) {
        if (state != State.SWINGING) {
            return 0.0f;
        }

        return Math.clamp(
                (animationTicks + tickDelta) / SWING_LENGTH,
                0.0f,
                1.0f
        );
    }

    public static float getRecoveryProgress(float tickDelta) {
        if (state != State.RECOVERY) {
            return 0.0f;
        }

        return Math.clamp(
                (animationTicks + tickDelta) / RECOVERY_LENGTH,
                0.0f,
                1.0f
        );
    }
}
