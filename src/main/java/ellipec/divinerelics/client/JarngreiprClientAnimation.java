package ellipec.divinerelics.client;

public class JarngreiprClientAnimation {

    private static int ticksRemaining = 0;

    public static final int ANIMATION_LENGTH = 15;

    public static void start() {
        ticksRemaining = ANIMATION_LENGTH;
    }

    public static void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
    }

    public static boolean isPlaying() {
        return ticksRemaining > 0;
    }

    public static int getTicksRemaining() {
        return ticksRemaining;
    }

    public static int getElapsedTicks() {
        return ANIMATION_LENGTH - ticksRemaining;
    }
}