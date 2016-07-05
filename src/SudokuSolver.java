import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Lord Daniel on 7/3/2016.
 */
public class SudokuSolver {

    public static ArrayList<ArrayList<Cell>> createGrid(int[][] plainGrid){
        ArrayList<ArrayList<Cell>> grid = new ArrayList<>();
        for (int i=0;i<9;i++){
            ArrayList<Cell> row = new ArrayList<>();
            for (int j=0;j<9;j++){
                int n = plainGrid[i][j];
                Cell cell;
                if(n==0) {
                    cell = new Cell(plainGrid[i][j], false);
                }
                else{
                    cell = new Cell(plainGrid[i][j], true);
                }
                row.add(cell);
            }
            grid.add(row);
        }

        //eliminate invalid elements from lists of possible values
        for(int x=0;x<9;x++) {
            for (int y = 0; y < 9; y++) {
                if (grid.get(x).get(y).getSolved()) {
                    //set possible values list of solved cells to empty
                    grid.get(x).get(y).setPossibleValuesEmpty();

                    int val = grid.get(x).get(y).getValue();

                    //ROW
                    for (int i = 0; i < 9; i++) {
                        grid.get(x).get(i).removeFromList(new Integer(val));
                    }

                    //COLUMN
                    for (int i = 0; i < 9; i++) {
                        grid.get(i).get(y).removeFromList(new Integer(val));
                    }

                    //3x3 region
                    int row1;
                    int row2;
                    if (x >= 0 && x <= 2) {
                        row1 = 0;
                        row2 = 2;
                    } else if (x >= 3 && x <= 5) {
                        row1 = 3;
                        row2 = 5;
                    } else {
                        row1 = 6;
                        row2 = 8;
                    }

                    int col1;
                    int col2;
                    if (y >= 0 && y <= 2) {
                        col1 = 0;
                        col2 = 2;
                    } else if ((y >= 3 && y <= 5)) {
                        col1 = 3;
                        col2 = 5;
                    } else {
                        col1 = 6;
                        col2 = 8;
                    }

                    for(int i=row1;i<=row2;i++) {
                        for (int j = col1; j <= col2; j++) {
                            grid.get(i).get(j).removeFromList(val);
                        }
                    }
                }
            }
        }


//        for (int i=0;i<9;i++){
//            for (int j=0;j<9;j++){
//                System.out.println("i: "+i+"  j: "+j+"  "+grid.get(i).get(j).getPossibleValues());
//            }
//        }
        return grid;
    }

    /**
     * This method takes in a grid of Cells where each cell has a list of possible values it could
     * take on and still be valid.
     * The method checks each row, each column and each 3x3 region to find an element that's not repeated. This
     * element must be part of the solution, so the value of that cell is set to that element, it is set to solved,
     * and that element is used to recursively reduce other lists of possible values.
     * @param grid
     * @return
     */
    public static int[][] simplifyPuzzle(ArrayList<ArrayList<Cell>> grid){
        //look for a list of possibilities with one element
        for (int i=0;i<9;i++) {
            for (int j = 0; j < 9; j++) {
                if(grid.get(i).get(j).getPossibleValues().size()==1){
                    //System.out.println("found");
                    grid = simplifyPuzzleRec(grid,i,j,(Integer)grid.get(i).get(j).getPossibleValues().get(0));
                }
            }
        }

        //ROWS
        for (int i=0;i<9;i++){
            HashMap<Integer,Integer> occurenceMap = new HashMap<>();
            ArrayList<Integer> uniqueElements = new ArrayList<>();
            //iterate through row
            for (int j=0;j<9;j++){
                for (Object k:grid.get(i).get(j).getPossibleValues()){
                    if(occurenceMap.containsKey(k)){
                        occurenceMap.put((Integer)k,occurenceMap.get(k)+1);
                    }
                    else{
                        occurenceMap.put((Integer)k,1);
                    }
                }
            }
            for(int m:occurenceMap.keySet()){
                if(occurenceMap.get(m)==1){
                    uniqueElements.add(m);
                }
            }
            //now we have the list of unique elements in this row
            //iterate through this row, and find cells with a unique element, set them solved
            for(int n=0;n>9;n++){
                for(int z:uniqueElements) {
                    if (grid.get(i).get(n).getPossibleValues().contains(z)){
                        grid = simplifyPuzzleRec(grid,i,n,grid.get(i).get(n).getValue());
                    }
                }
            }
        }

        //COLUMNS
        for (int i=0;i<9;i++){
            HashMap<Integer,Integer> occurenceMap = new HashMap<>();
            ArrayList<Integer> uniqueElements = new ArrayList<>();
            //iterate through row
            for (int j=0;j<9;j++){
                for (Object k:grid.get(j).get(i).getPossibleValues()){
                    if(occurenceMap.containsKey(k)){
                        occurenceMap.put((Integer)k,occurenceMap.get(k)+1);
                    }
                    else{
                        occurenceMap.put((Integer)k,1);
                    }
                }
            }
            for(int m:occurenceMap.keySet()){
                if(occurenceMap.get(m)==1){
                    uniqueElements.add(m);
                }
            }
            //now we have the list of unique elements in this column
            //iterate through this column, and find cells with a unique element, set them solved
            for(int n=0;n>9;n++){
                for(int z:uniqueElements) {
                    if (grid.get(n).get(i).getPossibleValues().contains(z)){
                        grid = simplifyPuzzleRec(grid,n,i,grid.get(n).get(i).getValue());
                    }
                }
            }
        }

        //look for a list of possibilities with one element
        for (int i=0;i<9;i++) {
            for (int j = 0; j < 9; j++) {
                if(grid.get(i).get(j).getPossibleValues().size()==1){
                    grid = simplifyPuzzleRec(grid,i,j,(Integer)grid.get(i).get(j).getPossibleValues().get(0));
                }
            }
        }

        //convert grid to 2D array of integers
        int[][] plainGrid = new int[9][9];
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                plainGrid[i][j] = grid.get(i).get(j).getValue();
            }
        }

        return plainGrid;
    }

    /**
     * Each time a list of possible values is reduced to size 1, that cell is set as solved and this method is
     * called on that cell recursively.
     *
     * @param grid
     * @return
     */
    public static ArrayList<ArrayList<Cell>> simplifyPuzzleRec(ArrayList<ArrayList<Cell>> grid, int x, int y, int val){
        //System.out.println("simplifyPuzzleRec() called.");
        grid.get(x).get(y).setValue(val);
        grid.get(x).get(y).setSolved();
        grid.get(x).get(y).setPossibleValuesEmpty();

        //eliminate this element (val) from every list in this row, column and 3x3 region.

        //ROW
        for (int i=0;i<9;i++){
            if(!grid.get(x).get(i).getSolved()) {
                grid.get(x).get(i).removeFromList(val);
                if (grid.get(x).get(i).getPossibleValues().size() == 1) {
                    simplifyPuzzleRec(grid, x, i, (Integer) grid.get(x).get(i).getPossibleValues().get(0));
                }
            }
        }

        //COLUMN
        for (int i=0;i<9;i++){
            if(!grid.get(i).get(y).getSolved()) {
                grid.get(i).get(y).removeFromList(val);
                if (grid.get(i).get(y).getPossibleValues().size() == 1) {
                    simplifyPuzzleRec(grid, i, y, (Integer) grid.get(i).get(y).getPossibleValues().get(0));
                }
            }
        }

        //3x3 region
        int row1;
        int row2;
        if (x>=0 && x <=2){
            row1 = 0; row2 = 2;
        }
        else if (x>=3 && x <=5){
            row1 = 3; row2 = 5;
        }
        else{
            row1 = 6; row2 = 8;
        }

        int col1;
        int col2;
        if (y>=0 && y <=2){
            col1 = 0; col2 = 2;
        }
        else if ((y>=3 && y <=5)){
            col1 = 3; col2 = 5;
        }
        else{
            col1 = 6; col2 = 8;
        }

        for(int i=row1;i<=row2;i++) {
            for (int j = col1; j <= col2; j++) {
                if (!grid.get(i).get(j).getSolved()) {
                    grid.get(i).get(j).removeFromList(val);
                    if (grid.get(i).get(j).getPossibleValues().size() == 1) {
                        simplifyPuzzleRec(grid, i, j, (Integer) grid.get(i).get(j).getPossibleValues().get(0));
                    }
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) throws FileNotFoundException {

        // construct the initial configuration from the file
        SudokuConfig puzzle = new SudokuConfig(args[0]);
        System.out.println(puzzle);

        ArrayList<ArrayList<Cell>> grid = createGrid(puzzle.getGrid());

        // start the clock
        double start = System.currentTimeMillis();

        // attempt to solve the puzzle
        Configuration init = new SudokuConfig(simplifyPuzzle(grid));
        //System.out.println("\nAfter simplification: \n\n"+init);

        Backtracker bt = new Backtracker(false);
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
