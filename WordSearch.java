import java. io. BufferedReader;
import java.io.FileReader;
import java. io. InputStreamReader;
import java. io. IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util. Iterator;
import java.util.List;


public class WordSearch
{
    public WordSearch( ) throws IOException
    {
    puzzleStream = openFile( "Enter puzzle file" );
    wordStream = openFile( "Enter dictionary name" );
    System.out.println( "Reading files..." );
    readPuzzle ();
    readWords ();
}
        public int solvePuzzle( )
        {
            int matches = 0;

            for ( int r = 0; r < rows; r++ )
                for ( int c = 0; c < columns; c++ )
                    for ( int rd = -1; rd <= 1; rd++ )
                        for ( int cd = -1; cd <= 1; cd++ )
                            if ( rd != 0 || cd != 0 )
                                matches += solveDirection( r, c, rd, cd );
            return matches;
        }
            private int rows;
            private int columns;
            private char theBoard[ ][ ];
            private String [ ] theWords;
            private BufferedReader puzzleStream;
            private BufferedReader wordStream;
            private BufferedReader in = new
                    BufferedReader( new InputStreamReader( System.in ) );
    private static int prefixSearch( String [ ] a, String x )
    {
        int idx = Arrays.binarySearch( a, x );

        if( idx < 0 )
            return -idx - 1;
        else
            return idx;
    }


    private BufferedReader openFile( String message )
    {
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;

        do
        {
            System.out.println( message + ": " );

            try
            {
                fileName = in.readLine( );
                if( fileName == null )
                    System.exit( 0 );
                theFile = new FileReader( fileName );
                fileIn  = new BufferedReader( theFile );
            }
            catch( IOException e )
            { System.err.println( "Cannot open " + fileName ); }
        } while( fileIn == null );

        System.out.println( "Opened " + fileName );
        return fileIn;
    }
    private void readWords( ) throws IOException
    {
        List<String> words = new ArrayList<String>( );

        String lastWord = null;
        String thisWord;

        while( ( thisWord = wordStream.readLine( ) ) != null )
        {
            if( lastWord != null && thisWord.compareTo( lastWord ) < 0 )
            {
                System.err.println( "Dictionary is not sorted... skipping" );
                continue;
            }
            words.add( thisWord );
            lastWord = thisWord;
        }

        theWords = new String[ words.size( ) ];
        theWords = words.toArray( theWords );
    }
    private void readPuzzle( ) throws IOException
    {
        String oneLine;
        List<String> puzzleLines = new ArrayList<String>( );

        if( ( oneLine = puzzleStream.readLine( ) ) == null )
            throw new IOException( "No lines in puzzle file" );

        columns = oneLine.length( );
        puzzleLines.add( oneLine );

        while( ( oneLine = puzzleStream.readLine( ) ) != null )
        {
            if( oneLine.length( ) != columns )
                System.err.println( "Puzzle is not rectangular; skipping row" );
            else
                puzzleLines.add( oneLine );
        }

        rows = puzzleLines.size( );
        theBoard = new char[ rows ][ columns ];

        int r = 0;
        for( String theLine : puzzleLines )
            theBoard[ r++ ] = theLine.toCharArray( );
    }
    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta) {
        int matches = 0;
        int row = baseRow + rowDelta;
        int col = baseCol + colDelta;
        int endRow = -1;
        int endCol = -1;

        while (isValidPosition(row, col)) {
            StringBuilder charSequence = new StringBuilder();
            charSequence.append(theBoard[baseRow][baseCol]);

            int r = baseRow;
            int c = baseCol;

            while (isValidPosition(r, c) && charSequence.length() <= theWords[0].length()) {
                if (prefixSearch(theWords, charSequence.toString()) != -1) {
                    matches++;
                    endRow = r;
                    endCol = c;
                }
                r += rowDelta;
                c += colDelta;

                if (isValidPosition(r, c)) {
                    charSequence.append(theBoard[r][c]);
                }
            }

            row += rowDelta;
            col += colDelta;
        }

        if (matches > 0) {
            System.out.println("Found " + theWords[0] + " at (" + baseRow + "," + baseCol + ") to (" + endRow + "," + endCol + ")");
        }

        return matches;
    }
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < columns;
    }

    public static void main( String [ ] args )
    {
        WordSearch p = null;

        try
        {
            p = new WordSearch( );
        }
        catch( IOException e )
        {
            System.out.println( "IO Error: " );
            e.printStackTrace( );
            return;
        }

        System.out.println( "Solving..." );
        p.solvePuzzle( );
    }

}