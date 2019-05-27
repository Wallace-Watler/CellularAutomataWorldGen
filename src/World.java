import java.awt.*;
import java.util.Random;

public final class World {

    private final int SEED = 0;
    private final int NUM_CHUNKS = 8;
    private final double FILL_AMOUNT = 0.5;
    private final int NUM_ITERATIONS = 2;

    private final Random rand = new Random(SEED);
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

        private static final int NUM_BLOCKS = 16;
        private final int BLOCK_SIZE = CHUNK_RENDER_SIZE / NUM_BLOCKS;
        private boolean[][] blocks;
        private boolean loaded;

        /**X and Y chunk position.*/
        private final int x, y;

        private Chunk(int x, int y) {
            this.x = x;
            this.y = y;
            blocks = new boolean[NUM_BLOCKS][NUM_BLOCKS];
            loaded = false;
        }

        private void render(Graphics g) {
            for(int j = 0; j < NUM_BLOCKS; j++) {
                for(int i = 0; i < NUM_BLOCKS; i++) {
                    g.setColor(Color.GREEN);
                    if(blocks[i][j]) g.fillRect(x * CHUNK_RENDER_SIZE + i * BLOCK_SIZE, y * CHUNK_RENDER_SIZE + j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        private void load() {
            if(!loaded) {
                randomlyPopulate();
                for(int i = 0; i < NUM_ITERATIONS; i++) iterate();
                loaded = true;
            }
        }

        private void randomlyPopulate() {
            for(int j = 0; j < NUM_BLOCKS; j++) {
                for(int i = 0; i < NUM_BLOCKS; i++) {
                    blocks[i][j] = rand.nextDouble() < FILL_AMOUNT;
                }
            }
        }

        private void iterate() {
            boolean[][] newGrid = new boolean[NUM_BLOCKS][NUM_BLOCKS];

            for(int j = 0; j < NUM_BLOCKS; j++) {
                for(int i = 0; i < NUM_BLOCKS; i++) {
                    int neighborCount = 0;
                    for(int n = j - 1; n <= j + 1; n++) {
                        for(int m = i - 1; m <= i + 1; m++) {
                            if(!(m == i && n == j) && n >= 0 && n < NUM_BLOCKS && m >= 0 && m < NUM_BLOCKS) {
                                neighborCount += blocks[m][n] ? 1 : 0;
                            }
                        }
                    }
                    if(neighborCount < 4) newGrid[i][j] = false;
                    else if(neighborCount > 4) newGrid[i][j] = true;
                    else newGrid[i][j] = blocks[i][j];
                }
            }

            blocks = newGrid;
        }
    }
}
