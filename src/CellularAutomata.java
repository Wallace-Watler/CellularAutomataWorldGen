import java.util.Random;

public class CellularAutomata {

    public static final IterativeFunction B5678S45678 = (grid, i, j) -> {
        int neighborCount = 0;
        for(int n = j - 1; n <= j + 1; n++) {
            for(int m = i - 1; m <= i + 1; m++) {
                if(!(m == i && n == j) && n >= 0 && n < 48 && m >= 0 && m < 48) {
                    neighborCount += grid[m][n];
                }
            }
        }
        if(neighborCount < 4) return 0;
        if(neighborCount > 4) return 1;
        return grid[i][j];
    };

    /**
     * Create and execute a cellular automaton for a particular chunk's world generation.
     * @return The final state of the CA in the chunk specified by {@code chunkX} and {@code chunkY}.
     */
    public static int[][] performCA(int worldSeed, int chunkX, int chunkY, int iterations, FillFunction fillFunction, IterativeFunction iterativeFunction) {
        // 3 x 3 chunk set, base chunk is in the center
        int[][] grid = new int[48][48];

        // Fill with initial values
        for(int chunkYOffset = -1; chunkYOffset <= 1; chunkYOffset++) {
            for(int chunkXOffset = -1; chunkXOffset <= 1; chunkXOffset++) {
                // TODO: Set up PRNG with chunk seed (currently just world seed; all chunks will generate the same)
                Random rand = new Random(worldSeed);
                for(int blockX = 0; blockX < 16; blockX++) {
                    for(int blockY = 0; blockY < 16; blockY++) {
                        grid[16 * (chunkXOffset + 1) + blockX][16 * (chunkYOffset + 1) + blockY] = fillFunction.calculateFill(rand);
                    }
                }
            }
        }

        // Iterate CA
        for(int iteration = 0; iteration < iterations; iteration++) {
            int[][] newGrid = new int[48][48];

            for(int j = 0; j < 48; j++) {
                for(int i = 0; i < 48; i++) {
                    newGrid[i][j] = iterativeFunction.calculateNextGen(grid, i, j);
                }
            }

            grid = newGrid;
        }

        // Return only the base chunk
        int[][] result = new int[16][16];
        for(int y = 0; y < 16; y++) {
            for(int x = 0; x < 16; x++) {
                result[x][y] = grid[x + 16][y + 16];
            }
        }
        return result;
    }

    @FunctionalInterface
    public interface FillFunction {
        /**
         * Defines the cells' initial values.
         * @param chunkPRNG - a PRNG seeded with the chunk seed
         * @return The cell's initial value.
         */
        int calculateFill(Random chunkPRNG);
    }

    @FunctionalInterface
    public interface IterativeFunction {
        /**
         * Determines how each cell interacts with its neighbors.
         * @param grid - the cell space (48 x 48)
         * @param i - the x-position of the cell
         * @param j - the y-position of the cell
         * @return The cell's new state.
         */
        int calculateNextGen(int[][] grid, int i, int j);
    }
}
