import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * Created by Lord Daniel on 7/3/2016.
 */
public class SudokuSolver {
    public static void main(String[] args) throws FileNotFoundException {

        // construct the initial configuration from the file
        Configuration init = new SudokuConfig(args[0]);

        System.out.println(init);

        Backtracker bt = new Backtracker(false);

        // start the clock
        double start = System.currentTimeMillis();

        // attempt to solve the puzzle
        Optional<Configuration> sol = bt.solve(init);

        // compute the elapsed time
        System.out.println("Elapsed time: " +
                (System.currentTimeMillis() - start)/1000.0 + " seconds.");

        // indicate whether there was a solution, or not
        if (sol.isPresent()) {
            System.out.println("SOLVED");
            System.out.println(sol.get());
        } else {
            System.out.println("No solution!");
        }
    }
}
