import java.awt.*;
import java.util.Random;

public class Chunk {

    private static final int NUM_BLOCKS = 16;
    private static final int BLOCK_SIZE = World.CHUNK_RENDER_SIZE / NUM_BLOCKS;
    private static final Random rand = new Random(0);
    private boolean[][] blocks;

    /**X and Y chunk position.*/
    private final int x, y;

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;
        blocks = new boolean[NUM_BLOCKS][NUM_BLOCKS];
    }

    public void randomlyPopulate(double fillAmount) {
        for(int y = 0; y < NUM_BLOCKS; y++) {
            for(int x = 0; x < NUM_BLOCKS; x++) {
                blocks[x][y] = rand.nextDouble() < fillAmount;
            }
        }
    }

    public void render(Graphics g) {
        for(int y = 0; y < NUM_BLOCKS; y++) {
            for(int x = 0; x < NUM_BLOCKS; x++) {
                g.setColor(Color.GREEN);
                if(blocks[x][y]) g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    public void tick() {
        boolean[][] newGrid = new boolean[NUM_BLOCKS][NUM_BLOCKS];

        for(int y = 0; y < NUM_BLOCKS; y++) {
            for(int x = 0; x < NUM_BLOCKS; x++) {
                int neighborCount = 0;
                for(int j = y - 1; j <= y + 1; j++) {
                    for(int i = x - 1; i <= x + 1; i++) {
                        if(!(i == x && j == y) && j >= 0 && j < NUM_BLOCKS && i >= 0 && i < NUM_BLOCKS) {
                            neighborCount += blocks[i][j] ? 1 : 0;
                        }
                    }
                }
                if(neighborCount < 4) newGrid[x][y] = false;
                else if(neighborCount > 4) newGrid[x][y] = true;
                else newGrid[x][y] = blocks[x][y];
            }
        }

        blocks = newGrid;
    }
}
