import java.awt.*;

public final class World {

    private static final int NUM_CHUNKS = 8;
    public static final int CHUNK_RENDER_SIZE = Main.WIDTH / NUM_CHUNKS;
    private static Chunk[][] chunks;

    public static void render(Graphics g) {
        for(int y = 0; y < NUM_CHUNKS; y++) {
            for(int x = 0; x < NUM_CHUNKS; x++) {
                if(chunks[x][y] != null) chunks[x][y].render(g);
            }
        }
    }
}
