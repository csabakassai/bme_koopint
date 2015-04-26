package env;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import actions.determine_winner.AuctionMode;

public class AuctionView extends JFrame {
	
	private final AuctionEnvironment environment;
	
	private final ActionEnvironmentPanel canvas;
	
	public AuctionView(AuctionEnvironment environment) throws HeadlessException {
		super();
		this.environment = environment;
		setSize( environment.environmentSize, environment.environmentSize );
		setVisible( true );
		getContentPane().setLayout( new BorderLayout() );
		canvas = new ActionEnvironmentPanel();
		System.out.println( "canvas" );
		getContentPane().add( canvas, BorderLayout.CENTER );
		
		JPanel buttonPanel = new JPanel( new GridLayout( 1, 4 ) );
		getContentPane().add( buttonPanel, BorderLayout.SOUTH );
		JButton miniSumButton = new JButton( "Mini sum" );
		miniSumButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent e ) {
				
				AuctionView.this.environment.startAuction( AuctionMode.MINI_SUM );
				
			}
		} );
		buttonPanel.add( miniSumButton, BorderLayout.SOUTH );
		
		JButton miniMaxButton = new JButton( "Mini max" );
		miniMaxButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent e ) {
				AuctionView.this.environment.startAuction( AuctionMode.MINI_MAX );
				
			}
		} );
		buttonPanel.add( miniMaxButton, BorderLayout.SOUTH );
		
		JButton addTaskButton = new JButton( "Add task" );
		addTaskButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent e ) {
				AuctionView.this.environment.addPoint();
				AuctionView.this.repaint();
				
			}
		} );
		buttonPanel.add( addTaskButton, BorderLayout.SOUTH );
		
		JButton resetButton = new JButton( "Reset" );
		resetButton.addActionListener( new ActionListener() {
			
			public void actionPerformed( ActionEvent e ) {
				AuctionView.this.environment.reset();
				
			}
		} );
		buttonPanel.add( resetButton, BorderLayout.SOUTH );
		
	}
	
	private class ActionEnvironmentPanel extends JPanel {
		
		@Override
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			Graphics2D g2d = (Graphics2D) g;
			
			for (Entry<String, Point> point : environment.getRobots().entrySet()) {
				
				String robotId = point.getKey();
				drawRobot( point.getValue(), g2d, robotId );
				Map<String, List<Point>> assignedTasks = environment.getKi();
				List<Point> tasks = assignedTasks.get( robotId );
				if (tasks != null) {
					Point temp = point.getValue();
					for (Point taskPosition : tasks) {
						drawLine( temp, taskPosition, g2d );
						temp = taskPosition;
					}
				}
				
			}
			
			for (Entry<String, Point> point : environment.getTasks().entrySet()) {
				drawTask( point.getValue(), g2d, point.getKey() );
			}
			
		}
		
		private void drawRobot( Point position, Graphics2D g2d, String name ) {
			g2d.setColor( Color.blue );
			g2d.fillOval( position.x, position.y, 12, 12 );
			g2d.drawString( name, position.x - 3, position.y - 3 );
		}
		
		private void drawTask( Point position, Graphics2D g2d, String id ) {
			g2d.setColor( Color.red );
			g2d.fillRect( position.x, position.y, 12, 12 );
			g2d.drawString( id, position.x - 3, position.y - 3 );
			
		}
		
		public void drawLine( Point from, Point to, Graphics2D g2d ) {
			g2d.setColor( Color.GREEN );
			g2d.drawLine( from.x + 6, from.y + 6, to.x + 6, to.y + 6 );
			
		}
	}
	
}
