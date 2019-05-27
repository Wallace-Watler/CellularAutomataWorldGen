import java.awt.*;

public final class World {

    private final int SEED = 0;
    private final int NUM_CHUNKS = 8;
    private final double FILL_AMOUNT = 0.5;
    private final int NUM_ITERATIONS = 2;

    private final int CHUNK_RENDER_SIZE = Main.WIDTH / NUM_CHUNKS;
    private Chunk[][] chunks = new Chunk[NUM_CHUNKS][NUM_CHUNKS];
    private int playerX = 4, playerY = 4;

    public static final World instance = new World();

    private World() {
        for(int y = 0; y < NUM_CHUNKS; y++) {
            for(int x = 0; x < NUM_CHUNKS; x++) {
                chunks[x][y] = new Chunk(x, y);
            }
        }
        chunks[playerX][playerY].load();
    }

    public void render(Graphics g) {
        for(int y = 0; y < NUM_CHUNKS; y++) {
            for(int x = 0; x < NUM_CHUNKS; x++) {
                if(chunks[x][y] != null) chunks[x][y].render(g);
            }
        }
        g.setColor(Color.YELLOW);
        g.drawRect(playerX * CHUNK_RENDER_SIZE, playerY * CHUNK_RENDER_SIZE, CHUNK_RENDER_SIZE, CHUNK_RENDER_SIZE);
    }

    public void movePlayer(int x, int y) {
        if(playerX + x >= 0 && playerX + x < NUM_CHUNKS) playerX += x;
        if(playerY + y >= 0 && playerY + y < NUM_CHUNKS) playerY += y;
        chunks[playerX][playerY].load();
    }

    private final class Chunk {

        private final int BLOCK_SIZE = CHUNK_RENDER_SIZE / 16;
        private boolean[][] blocks;
        private boolean loaded;

        /**X and Y chunk position.*/
        private final int x, y;

        private Chunk(int x, int y) {
            this.x = x;
            this.y = y;
            blocks = new boolean[16][16];
            loaded = false;
        }

        private void render(Graphics g) {
            for(int j = 0; j < 16; j++) {
                for(int i = 0; i < 16; i++) {
                    g.setColor(Color.GREEN);
                    if(blocks[i][j]) g.fillRect(x * CHUNK_RENDER_SIZE + i * BLOCK_SIZE, y * CHUNK_RENDER_SIZE + j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        private void load() {
            if(!loaded) {
                int[][] resultCA = CellularAutomata.performCA(SEED, x, y, NUM_ITERATIONS, chunkPRNG -> chunkPRNG.nextDouble() < FILL_AMOUNT ? 1 : 0, CellularAutomata.B5678S45678);
                for(int j = 0; j < 16; j++) {
                    for(int i = 0; i < 16; i++) {
                        blocks[i][j] = resultCA[i][j] == 1;
                    }
                }
                loaded = true;
            }
        }
    }
}
