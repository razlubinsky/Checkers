import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

	public class Board implements MouseListener
    {
		public enum Direction 
		{
			RIGHT_UP,RIGHT_DOWN,LEFT_UP,LEFT_DOWN,NONE
		}	
        private JFrame frame = new JFrame();
        private JPanel backBoard = new JPanel();
        private BoardSquare board[][];
        private boolean isFirst = true;
        private BoardSquare lastTile;
        private boolean isPlayerOneTurn = true;
	    private Direction direction = Direction.NONE;
	    private boolean isSameSpot = false;
	    private int jump = 0;
	    private boolean isKill = false;
	    private boolean isOver = false;
	    private ArrayList<BoardSquare> legal = new ArrayList<BoardSquare>();
	    private boolean isCheck = false;
	    
        Board()
	    {
        	board = new BoardSquare[8][8];
	        frame.setSize(905,905);
	        backBoard.setSize(900,900);
	        frame.setTitle("Checkers");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
	        backBoard.setVisible(true);
	        backBoard.addMouseListener(this);
	        
	        Type type;
	    	for (int r = 0; r< 8 ; r++)
	    	{
	    		for (int c = 0 ; c<8 ; c++)
	    		{
	    			if (((r + c) % 2) == 0 && (r<3 ))
	    				type = Type.RED;
	    			else if (((r + c) % 2) == 0 && (r>4 ))
	    				type = Type.BLACK;
	    			else
	    				type=Type.BLANK;

	                BoardSquare box = new BoardSquare(r,c,type);
	                box.addMouseListener(this);
	                board[r][c]= box;
	                backBoard.add(box);
	            }           
	        }

	        frame.add(backBoard);

	    }
        
        
        public boolean getIsSameSpot()
        {
        	return isSameSpot;
        }
	    public boolean checkPlayerOneTurn()
	    {
	    	return isPlayerOneTurn;
	    }
	    //change turn from one player to another
	    public void changeTurn()
	    {
	    	isPlayerOneTurn = !isPlayerOneTurn;
	    	if (isPlayerOneTurn)
	    		System.out.println("player one its your turn");
	    	else
		    	System.out.println("player two its your turn");
	    }
	    //checks if the move was of the right player
		public boolean isCorrectPlayer(BoardSquare tile)
		{
			//if its player one turn
			if (isFirst)
			{
				//if its his first click, check for right color of checker
				if (checkPlayerOneTurn())
				{
					if (tile.getSqareType() == Type.RED || tile.getSqareType() == Type.QUEEN_RED)
					{
						return true;
					}
				}
				//check player two
				else 
				{
					if (tile.getSqareType() == Type.BLACK || tile.getSqareType() == Type.QUEEN_BLACK)
					{
						return true;
					}
				}
			}
			//if its not his first click, check for empty tile no matter the player
			else
			{
				if (tile.getSqareType() == Type.BLINK_BLANK || tile.getSqareType() == Type.BLANK)
				{
					if (isPlayerOneTurn)
					{
						if (lastTile.getSqareType() == Type.BLINK_RED || lastTile.getSqareType() == Type.QUEEN_RED_BLINK )
						{
							return true;
						}
					}
					else
					{
						if (lastTile.getSqareType() == Type.BLINK_BLACK || lastTile.getSqareType() == Type.QUEEN_BLACK_BLINK )
						{
							return true;
						}
					}
				}
				else 
				{
					if(tile.getSqareType() == lastTile.getSqareType())
						return true;
				}

			}
			return false;
		}
		public boolean isPieceThere(int row, int col, Type type)
		{
			return (board[row][col].getSqareType() == type);
		}
		public boolean checkCounters(int black,int red)
		{
			if (isPlayerOneTurn)
			{
				if (red != 0 || black >= 2)
					return false;
				if (black == 1 && isCheck == false)
					isKill = true;
			}
			else
			{
				if (black != 0 || red >= 2)
					return false;			
				if (red == 1 && isCheck == false)
					isKill = true;
			}
			return true;
		}
		public boolean checkDirection(int fixRow,int fixCol,int steps)
		{
			int countBlack = 0;
			int countRed = 0;
			for (int i = 1; i<steps;i++)
			{
				if (((isPieceThere(lastTile.getRow() + (i*fixRow),lastTile.getCol() + (i*fixCol),Type.RED)) || (isPieceThere(lastTile.getRow() + (i*fixRow),lastTile.getCol() + (i*fixCol),Type.QUEEN_RED))))
					countRed++;
				if (((isPieceThere(lastTile.getRow() + (i*fixRow),lastTile.getCol() + (i*fixCol),Type.BLACK)) || (isPieceThere(lastTile.getRow() + (i*fixRow),lastTile.getCol() + (i*fixCol),Type.QUEEN_BLACK))))
					countBlack++;
			}
			if (isCheck == false)
				jump = steps-1;
			return checkCounters(countBlack,countRed);
		}
		//checks if the jumps are legal by the spaces they skip
		public boolean checkSpaces(Direction direction,int steps)
		{
			int fixRow = 1;
			int fixCol = 1;
			if (direction == Direction.LEFT_DOWN)
			{
				fixCol *= -1;
				return checkDirection(fixRow,fixCol,steps);
			}
			if (direction == Direction.LEFT_UP)
			{
				fixRow *= -1;
				fixCol *= -1;
				return checkDirection(fixRow,fixCol,steps);								
			}
			if (direction == Direction.RIGHT_DOWN)
			{
				return checkDirection(fixRow,fixCol,steps);
			}
			if (direction == Direction.RIGHT_UP)
			{
				fixRow *= -1;
				return checkDirection(fixRow,fixCol,steps);
			}	
			return true;
		}
		public boolean isNumberOfStepsCorrectForQueen(BoardSquare tile)
		{
			if(!isFirst)
	    	{
    			int subRows = lastTile.getRow() - tile.getRow();
    			int subCols = lastTile.getCol() - tile.getCol(); 

   				if ((Math.abs(subRows)) - (Math.abs(subCols)) == 0 )
   				{
   					if (subRows > 0 && subCols < 0)
   						return checkSpaces(direction = Direction.RIGHT_UP ,Math.abs(subRows));
   					if(subRows < 0 && subCols < 0)
   						return checkSpaces(direction = Direction.RIGHT_DOWN, Math.abs(subRows));
   					if(subRows > 0 && subCols > 0)
   						return checkSpaces(direction = Direction.LEFT_UP, Math.abs(subRows));
   					if(subRows < 0 && subCols > 0)
   						return checkSpaces(direction = Direction.LEFT_DOWN, Math.abs(subRows));
   				}
   				else
   					return false;
    			
    			
	    	}
			//if its first click
			return true;
		}

	    public boolean isNumberOfStepsCorrect(BoardSquare tile)
	    {
	    	if(!isFirst)
	    	{
	    		if(lastTile.getSqareType() == Type.QUEEN_BLACK_BLINK || lastTile.getSqareType() == Type.QUEEN_RED_BLINK)
	    		{
	    			return isNumberOfStepsCorrectForQueen(tile);
	    		}
	    		else
	    		{
	    			int subRows = lastTile.getRow() - tile.getRow();
	    			int subCols = lastTile.getCol() - tile.getCol();
	    			if (subRows == 0 && subCols == 0)
	    				return true;
	    			if (isPlayerOneTurn)
	    			{
	    				//if its player one than he can only go down the rows
	    				if(  (subRows == -1) && ((subCols == 1) || (subCols == -1))	 )
	    				{
	    					return true;
	    				}
	    				//first player - jumps down to the left
	    				if(  (((subRows) == -2) ) && (subCols == 2) )
	    				{
	    					if (((isPieceThere(lastTile.getRow() + 1,lastTile.getCol() - 1,Type.BLACK)) || (isPieceThere(lastTile.getRow() + 1,lastTile.getCol() - 1,Type.QUEEN_BLACK))))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.LEFT_DOWN;
	    						
	    						return true;
	    					}
	    					return false;
	    				}
	    				//first player - jumps down to the right
	    				if(  (((subRows) == -2) ) && (subCols == -2) )
	    				{
	    					if ((isPieceThere(lastTile.getRow() + 1,lastTile.getCol() + 1,Type.BLACK)) || (isPieceThere(lastTile.getRow() + 1,lastTile.getCol() + 1,Type.QUEEN_BLACK)))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.RIGHT_DOWN;
	    						return true;
	    					}
	    					return false;
	    				}
	    				//if isKill == true you can eat back too
	    				if(  (((subRows) == 2) ) && (subCols == 2) && (isKill == true) )
	    				{
	    					if ((isPieceThere(lastTile.getRow() - 1,lastTile.getCol() - 1,Type.BLACK)) || (isPieceThere(lastTile.getRow() - 1,lastTile.getCol() - 1,Type.QUEEN_BLACK)))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.LEFT_UP;
	    						return true;
	    					}
	    				}
	    				//Second player - jumps up to the right
	    				if(  (((subRows) == 2) ) && (subCols == -2) && (isKill == true))
	    				{
	    					if((isPieceThere(lastTile.getRow() - 1,lastTile.getCol() + 1,Type.BLACK)) || (isPieceThere(lastTile.getRow() - 1,lastTile.getCol() + 1,Type.QUEEN_BLACK)))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.RIGHT_UP;
	    						return true;
	    					}
	    					return false;		
	    				}
	    				
	    				
	    				
	    			}
	    			else if (!isPlayerOneTurn)
	    			{
	    				//if its player Two than he can only go up the rows
	    				if(  (subRows == 1) && ((subCols == 1) || (subCols  == -1))	 )
	    				{
	    					return true;
	    				}
	    				//Second player - jumps up to the left
	    				if(  (((subRows) == 2) ) && (subCols == 2) )
	    				{
	    					if ((isPieceThere(lastTile.getRow() - 1,lastTile.getCol() - 1,Type.RED)) || (isPieceThere(lastTile.getRow() - 1,lastTile.getCol() - 1,Type.QUEEN_RED)))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.LEFT_UP;
	    						return true;
	    					}
	    				}
	    				//Second player - jumps up to the right
	    				if(  (((subRows) == 2) ) && (subCols == -2) )
	    				{
	    					if((isPieceThere(lastTile.getRow() - 1,lastTile.getCol() + 1,Type.RED)) || (isPieceThere(lastTile.getRow() - 1,lastTile.getCol() + 1,Type.QUEEN_RED)))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.RIGHT_UP;
	    						return true;
	    					}
	    					return false;		
	    				}
	    				//first player - jumps down to the left
	    				if(  (((subRows) == -2) ) && (subCols == 2) && (isKill == true) )
	    				{
	    					if (((isPieceThere(lastTile.getRow() + 1,lastTile.getCol() - 1,Type.RED)) || (isPieceThere(lastTile.getRow() + 1,lastTile.getCol() - 1,Type.QUEEN_RED))))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.LEFT_DOWN;
	    						return true;
	    					}
	    					return false;
	    				}
	    				//first player - jumps down to the right
	    				if(  (((subRows) == -2) ) && (subCols == -2) && (isKill==true))
	    				{
	    					if ((isPieceThere(lastTile.getRow() + 1,lastTile.getCol() + 1,Type.RED)) || (isPieceThere(lastTile.getRow() + 1,lastTile.getCol() + 1,Type.QUEEN_RED)))
	    					{
	    						if (isCheck==false)
	    						{
	    							isKill = true;
	    							jump = 1;
	    						}
	    						direction = Direction.RIGHT_DOWN;
	    						return true;
	    					}
	    					return false;
	    				}
	    			}
	    			return false;
	    		}
	    	}
			return true;
	   	}
	    public boolean isLegalMove(BoardSquare tile)
	    {
	    	//checks player 1 click1 on red, player 2 click1 on black, and for click2 blank
	    	if(isCorrectPlayer(tile))
	    	{
	    		if(isNumberOfStepsCorrect(tile))
	    		{
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    public void startTheGame()
	    {
	    	System.out.println("start the game");
	    	if (isPlayerOneTurn)
	    		System.out.println("player one its your turn");
	    	else
	    		System.out.println("player two its your turn");
	    	
	    }
	    public void makeJump()
	    {
	    	if (direction == Direction.LEFT_DOWN)
	    		for (int i = 1;i<= jump; i++)
	    			board[lastTile.getRow() + i][lastTile.getCol() - i].setType(Type.BLANK);
	    	if (direction == Direction.RIGHT_DOWN)
	    		for (int i = 1;i<= jump+1; i++)
	    			board[lastTile.getRow() + i][lastTile.getCol() + i].setType(Type.BLANK);
	    	if (direction == Direction.LEFT_UP)
	    		for (int i = 1;i<= jump; i++)
	    			board[lastTile.getRow() - i][lastTile.getCol() - i].setType(Type.BLANK);
	    	if (direction == Direction.RIGHT_UP)
	    		for (int i = 1;i<= jump; i++)
	    			board[lastTile.getRow() - i][lastTile.getCol() + i].setType(Type.BLANK);
	    }
	    public void makeMove(BoardSquare tile)
	    {
			//if its player one paint the tile in red
			if (checkPlayerOneTurn())
			{
				if(tile.getRow() == 7)
					tile.setType(Type.QUEEN_RED);
				else
				{
					if (lastTile.getSqareType() == Type.QUEEN_RED_BLINK)
						tile.setType(Type.QUEEN_RED);
					else
					{
						tile.setType(Type.RED);
					}
				}
			}
			//else paint the tile in black
			else
			{
				if(tile.getRow() == 0)
					tile.setType(Type.QUEEN_BLACK);
				else
				{
					if (lastTile.getSqareType() == Type.QUEEN_BLACK_BLINK)
					{
						tile.setType(Type.QUEEN_BLACK);
					}
					else
					{
						tile.setType(Type.BLACK);
					}
				}
			}
	    }
	    public boolean checkForSameClick(BoardSquare tile)
	    {
	    	if (!isFirst)
	    	{
	    		if (tile.getSqareType() == lastTile.getSqareType())
	    		{
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    public void blinkingOff(BoardSquare tile)
	    {
	    	if (!checkForSameClick(tile))
	    	{
		    	if(tile.getSqareType()==Type.BLINK_BLANK)
		    		tile.setType(Type.BLANK);
	    	}
	    }
	    
	    
	    public void blinkingOn(BoardSquare tile)
	    {
			if (checkPlayerOneTurn())
			{
				if (tile.getSqareType() == Type.QUEEN_RED)
					tile.setType(Type.QUEEN_RED_BLINK);
				else if (tile.getSqareType() == Type.RED)
					tile.setType(Type.BLINK_RED);
				else if (tile.getSqareType() == Type.BLANK)
					tile.setType(Type.BLINK_BLANK);
			}
			else
			{
				if (tile.getSqareType() == Type.QUEEN_BLACK)
					tile.setType(Type.QUEEN_BLACK_BLINK);
				else if(tile.getSqareType() == Type.BLACK)
					tile.setType(Type.BLINK_BLACK);
				else if(tile.getSqareType() == Type.BLANK)
					tile.setType(Type.BLINK_BLANK);
			}
			frame.repaint();
			lastTile = tile;

	    }
	    
	    public void checkForAnotherTurn(BoardSquare tile)
	    {
	    	makeJump();
	    	makeMove(tile);
	    	//turnOffLegalMoves();
	    	lastTile.setType(Type.BLANK);
	    	direction = Direction.NONE;
	    	jump=0;

	    	blinkingOn(tile);
	    	//isKill = false;
	    	isFirst = !isFirst;
	    	changeTurn();
	    }
	    
	    public void Victory()
	    {
	    	int counterRed = 0;
	    	int counterBlack = 0;
	    	for (int i = 0; i < 8 ;i++)
	    	{
	    		for(int j = 0; j<8; j++)
	    		{
	    			if (board[i][j].getSqareType()==Type.RED || board[i][j].getSqareType()==Type.QUEEN_RED || board[i][j].getSqareType()==Type.BLINK_RED || board[i][j].getSqareType()==Type.QUEEN_RED_BLINK)
	    				counterRed++;
	    			else if (board[i][j].getSqareType()==Type.BLACK || board[i][j].getSqareType()==Type.QUEEN_BLACK || board[i][j].getSqareType()==Type.BLINK_BLACK || board[i][j].getSqareType()==Type.QUEEN_BLACK_BLINK)
	    				counterBlack++;
	    		}
	    	}
	    	if (counterBlack == 0)
	    	{
	    		System.out.println("Player One wins!!!!");
	    		isOver=true;
	    	}
	    	if (counterRed == 0)
	    	{
	    		System.out.println("Player Two wins!!!!");
	    		isOver=true;
	    	}
	    }
	    public void turnOffLegalMoves()
	    {
	    	for (int i = 0; i<legal.size(); i++)
	    		blinkingOff(legal.get(i));
	    	

	    }
	    public void turnOnLegalMoves()
	    {
	    	for (BoardSquare tile: legal) 
	    	{
	    		blinkingOn(tile);
	    	}
	    }
	    public void legalMoves(BoardSquare tile)
	    {
	    	isCheck = true;
	    	lastTile = tile;
	    	blinkingOn(lastTile);
	    	while (!legal.isEmpty())
	    	{
	    		legal.remove(0);
	    	}
   	    	isFirst = !isFirst;
	    	for(int i =0; i<8; i++)
	    	{
	    		for (int j =0; j<8 ; j++)
	    		{
	    			if (isLegalMove(board[i][j]))
	    				legal.add(board[i][j]);
	    		}
	    	}
	    	isCheck = false;
	    	isFirst = !isFirst;
	    	
	    }
		public void mouseClicked(MouseEvent arg0) 
		{
			if (isOver == false)
			{
				BoardSquare tile = (BoardSquare) arg0.getComponent();
				if(isLegalMove(tile))
				{
					if (isSameSpot == false )
					{	
						//first click 
						if (isFirst == true)
						{
							legalMoves(tile);
							turnOnLegalMoves();
							lastTile = tile;
						}
						//not first click
						else
						{
							frame.repaint();
							//if player clicked on the same location again and kill = true - stop.
							if (checkForSameClick(tile) && isKill == true)
							{
								makeMove(tile);
								turnOffLegalMoves();
								frame.repaint();
								isKill = false;
							}
							//if player clicked on the same location again and kill = false - still his turn.					
							else if (checkForSameClick(tile) && isKill == false)
							{
								makeMove(tile);
								turnOffLegalMoves();
								frame.repaint();
								changeTurn();
							}
							//if its not the same location again and kill = false and no jump (just move)
							else if(jump==0 && isKill == false)
							{
								makeMove(tile);
								turnOffLegalMoves();
					    		lastTile.setType(Type.BLANK);
								frame.repaint();
							}
							//kill = false, there is a jump (do it and stop)
							else if(jump!=0 && isKill== false)
							{
								makeMove(tile);
								turnOffLegalMoves();
								direction = Direction.NONE;
								lastTile.setType(Type.BLANK);
								frame.repaint();
								jump=0;
							}
							//kill = true, there is a jump (his turn again)
							else if (isKill == true && jump != 0)
							{
								if((lastTile.getSqareType() == Type.QUEEN_BLACK_BLINK) || (lastTile.getSqareType() == Type.QUEEN_RED_BLINK))
								{
									isKill = false;
									isNumberOfStepsCorrectForQueen(tile);
									if (isKill == true)
										checkForAnotherTurn(tile);
									else
									{
										isFirst = !isFirst;
										changeTurn();
										isKill = true;
									}
								}
								else
								{
									checkForAnotherTurn(tile);
								}
								jump=0;
							}
							//there isn't a jump - stop
							else
							{
								isFirst = !isFirst;
								changeTurn();
							}
							changeTurn();
						}
						isFirst = !isFirst;
					}
					
				
				}
			}
			Victory();
		}
		
		public static void main(String[] args)
		{
			Board game = new Board();
			game.startTheGame();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) 
		{
		
		}
		@Override
		public void mouseExited(MouseEvent arg0) 
		{
			
		}
		@Override
		public void mousePressed(MouseEvent arg0) 
		{
			
		}
		@Override
		public void mouseReleased(MouseEvent arg0) 
		{
			
		}
    }
