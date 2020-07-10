package homeinventory;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Color;

import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.beans.PropertyChangeEvent;

public class HomeInventory extends JFrame 
{
	GridBagConstraints constraint;
	
	JToolBar inventoryToolBar = new JToolBar();
	
	JButton newButton = new JButton(new ImageIcon(HomeInventory.class.getResource("/Icons/new.gif")));
	JButton deleteButton = new JButton(new ImageIcon(HomeInventory.class.getResource("/Icons/delete.gif")));
	JButton saveButton = new JButton(new ImageIcon(HomeInventory.class.getResource("/Icons/save.gif")));
	JButton previousButton = new JButton(new ImageIcon(HomeInventory.class.getResource("/Icons/previous.gif")));
	JButton nextButton = new JButton(new ImageIcon(HomeInventory.class.getResource("/Icons/next.gif")));
	JButton printButton = new JButton(new ImageIcon(HomeInventory.class.getResource("/Icons/print.gif")));
	JButton exitButton = new JButton();
	
	JLabel itemLabel = new JLabel();
	JTextField itemTextField = new JTextField();
	JLabel locationLabel = new JLabel();
	JComboBox locationComboBox = new JComboBox();
	JCheckBox markedCheckBox = new JCheckBox();
	JLabel serialLabel = new JLabel();
	JTextField serialTextField = new JTextField();
	JLabel priceLabel = new JLabel();
	JTextField priceTextField = new JTextField();
	JLabel dateLabel = new JLabel();
	JDateChooser dateDateChooser = new JDateChooser();
	JLabel storeLabel = new JLabel();
	JTextField storeTextField = new JTextField();
	JLabel noteLabel = new JLabel();
	JTextField noteTextField = new JTextField();
	JLabel photoLabel = new JLabel();
	static JTextArea photoTextArea = new JTextArea();
	JButton photoButton = new JButton();
	JPanel searchPanel = new JPanel();
	JButton[] searchButton = new JButton[26];
	
	PhotoPanel photoPanel = new PhotoPanel();
	
	final static int maximumEntries = 300;
	static int numberEntries;
	static InventoryItem[] myInventory = new InventoryItem[maximumEntries];
	int n,currentEntry,i,j;
	
	static final int entriesPerPage = 2;
	static int lastPage;
	
	public static void main(String[] args) 
	{
		new HomeInventory().show();
	}
	
	public HomeInventory() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exitForm(arg0);
			}
		});
		setResizable(false);
		setTitle("Home Inventory Manager");
		GridBagLayout gridBagLayout = new GridBagLayout();
		getContentPane().setLayout(gridBagLayout);
		
		constraint= new GridBagConstraints();
		constraint.anchor = GridBagConstraints.NORTH;
		constraint.gridheight = 8;
		constraint.gridx = 0;
		constraint.gridy = 0;
		inventoryToolBar.setOrientation(SwingConstants.VERTICAL);
		inventoryToolBar.setBackground(Color.BLUE);
		inventoryToolBar.setFloatable(false);
		getContentPane().add(inventoryToolBar, constraint);
		
		inventoryToolBar.addSeparator();

		Dimension bSize = new Dimension(70, 50);
		
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newButtonActionPerformed(e);
			}
		});
		newButton.setText("New");
		sizeButton(newButton, bSize);
		newButton.setToolTipText("Add New Item");
		newButton.setHorizontalTextPosition(SwingConstants.CENTER);
		newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		newButton.setFocusable(false);
		inventoryToolBar.add(newButton);
		
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteButtonActionPerformed(e);
			}

		});
		deleteButton.setText("Delete");
		sizeButton(deleteButton, bSize);
		deleteButton.setToolTipText("Delete Current Item");
		deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
		deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		deleteButton.setFocusable(false);
		inventoryToolBar.add(deleteButton);
		
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed(e);
			}
		});
		saveButton.setText("Save");
		sizeButton(saveButton, bSize);
		saveButton.setToolTipText("Save Current Item");
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.setFocusable(false);
		inventoryToolBar.add(saveButton);
		
		inventoryToolBar.addSeparator();
		
		
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previousButtonActionPerformed(e);
			}
		});
		previousButton.setText("Previous");
		sizeButton(previousButton, bSize);
		previousButton.setToolTipText("Display Previous Item");
		previousButton.setHorizontalTextPosition(SwingConstants.CENTER);
		previousButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		previousButton.setFocusable(false);
		inventoryToolBar.add(previousButton);
		
		
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextButtonActionPerformed(e);
			}
		});
		nextButton.setText("Next");
		sizeButton(nextButton, bSize);
		nextButton.setToolTipText("Display Next Item");
		nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		nextButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		nextButton.setFocusable(false);
		inventoryToolBar.add(nextButton);
		
		inventoryToolBar.addSeparator();
		
		
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printButtonActionPerformed(e);
			}
		});
		printButton.setText("Print");
		sizeButton(printButton, bSize);
		printButton.setToolTipText("Print Inventory List");
		printButton.setHorizontalTextPosition(SwingConstants.CENTER);
		printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		printButton.setFocusable(false);
		inventoryToolBar.add(printButton);
		
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exitButtonActionPerformed();
			}
		});
		exitButton.setText("Exit");
		sizeButton(exitButton, bSize);
		exitButton.setToolTipText("Exit Program");
		exitButton.setFocusable(false);
		inventoryToolBar.add(exitButton);
		
		itemLabel.setText("Inventory Item");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 0;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(itemLabel, constraint);
		
		
		itemTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				itemTextFieldActionPerformed(arg0);
			}
		});
		itemTextField.setPreferredSize(new Dimension(400, 25));
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 0;
		constraint.gridwidth = 5;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(itemTextField, constraint);
		
		locationLabel.setText("Location");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(locationLabel, constraint);
		
		
		locationComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationComboBoxActionPerformed(e);
			}
		});
		locationComboBox.setPreferredSize(new Dimension(270, 25));
		locationComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		locationComboBox.setEditable(true);
		locationComboBox.setBackground(Color.WHITE);
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 1;
		constraint.gridwidth = 3;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(locationComboBox, constraint);
		
		markedCheckBox.setText("Marked?");
		markedCheckBox.setFocusable(false);
		constraint = new GridBagConstraints();
		constraint.gridx = 5;
		constraint.gridy = 1;
		constraint.insets = new Insets(10, 10, 0, 0);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(markedCheckBox, constraint);
		
		serialLabel.setText("Serial Number");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 2;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(serialLabel, constraint);
		
		
		serialTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serialTextFieldActionPerformed(e);
			}
		});
		serialTextField.setPreferredSize(new Dimension(270, 25));
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 2;
		constraint.gridwidth = 3;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(serialTextField, constraint);
		
		priceLabel.setText("Purchase Price");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 3;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(priceLabel, constraint);
		
		
		priceTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				priceTextFieldActionPerformed(e);
			}
		});
		priceTextField.setPreferredSize(new Dimension(160, 25));
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 3;
		constraint.gridwidth = 2;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(priceTextField, constraint);
		
		dateLabel.setText("Date Purchased");
		constraint = new GridBagConstraints();
		constraint.gridx = 4;
		constraint.gridy = 3;
		constraint.insets = new Insets(10, 10, 0, 0);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(dateLabel, constraint);
		
		
		dateDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				dateDateChooserPropertyChange(arg0);
			}
		});
		dateDateChooser.setPreferredSize(new Dimension(120, 25));
		constraint = new GridBagConstraints();
		constraint.gridx = 5;
		constraint.gridy = 3;
		constraint.gridwidth = 2;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(dateDateChooser, constraint);
		
		storeLabel.setText("Store/Website");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 4;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(storeLabel, constraint);
		
		
		storeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				storeTextFieldActionPerformed(e);
			}
		});	
		storeTextField.setPreferredSize(new Dimension(400, 25));
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 4;
		constraint.gridwidth = 5;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(storeTextField, constraint);
		
		noteLabel.setText("Note");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 5;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(noteLabel, constraint);
		
		
		noteTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noteTextFieldActionPerformed(e);
			}
		});
		
		noteTextField.setPreferredSize(new Dimension(400, 25));
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 5;
		constraint.gridwidth = 5;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(noteTextField, constraint);
		
		photoLabel.setText("Photo");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 6;
		constraint.insets = new Insets(10, 10, 0, 10);
		constraint.anchor = GridBagConstraints.EAST;
		getContentPane().add(photoLabel, constraint);
		
		photoTextArea.setPreferredSize(new Dimension(350, 35));
		photoTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
		photoTextArea.setEditable(false);
		photoTextArea.setLineWrap(true);
		photoTextArea.setWrapStyleWord(true);
		photoTextArea.setBackground(new Color(255, 255, 192));
		photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		photoTextArea.setFocusable(false);
		constraint = new GridBagConstraints();
		constraint.gridx = 2;
		constraint.gridy = 6;
		constraint.gridwidth = 4;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(photoTextArea, constraint);
		
		
		photoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				photoButtonActionPerformed(arg0);
			}
		});
		photoButton.setText("...");
		constraint = new GridBagConstraints();
		constraint.gridx = 6;
		constraint.gridy = 6;
		constraint.insets = new Insets(10, 0, 0, 10);
		constraint.anchor = GridBagConstraints.WEST;
		getContentPane().add(photoButton, constraint);
		
		searchPanel.setPreferredSize(new Dimension(240, 160));
		searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
		searchPanel.setLayout(new GridBagLayout());
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 7;
		constraint.gridwidth = 3;
		constraint.insets = new Insets(10, 0, 10, 0);
		constraint.anchor = GridBagConstraints.CENTER;
		getContentPane().add(searchPanel, constraint);
		
		int x = 0, y = 0;
		
		for (int i = 0; i < 26; i++)
		{
			searchButton[i] = new JButton();
			searchButton[i].setText(String.valueOf((char) (65 + i)));
			searchButton[i].setFont(new Font("Arial", Font.BOLD, 12));
			searchButton[i].setMargin(new Insets(-10, -10, -10, -10));
			searchButton[i].setFocusable(false);
			sizeButton(searchButton[i],new Dimension(37, 27));
			searchButton[i].setBackground(Color.YELLOW);
			constraint = new GridBagConstraints();
			constraint.gridx = x;
			constraint.gridy = y;
			searchPanel.add(searchButton[i], constraint);
			
			searchButton[i].addActionListener(new ActionListener (){
				public void actionPerformed(ActionEvent e){
					searchButtonActionPerformed(e);		
				}
			});
			
			x++;
			
			if (x % 6 == 0)
			{
				x = 0;
				y++;
			}
		}
		
		photoPanel.setPreferredSize(new Dimension(240, 160));
		constraint = new GridBagConstraints();
		constraint.gridx = 4;
		constraint.gridy = 7;
		constraint.gridwidth = 3;
		constraint.insets = new Insets(10, 0, 10, 10);
		constraint.anchor = GridBagConstraints.CENTER;
		getContentPane().add(photoPanel, constraint);
	
		pack();
		
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int) (0.5 * (screenSize.width - getWidth())), (int) (0.5 * (screenSize.height- getHeight())), getWidth(), getHeight());
		
		int n;
		try
		{
			BufferedReader inputFile = new BufferedReader(new FileReader("inventory.txt"));
			numberEntries =Integer.valueOf(inputFile.readLine()).intValue();
			if (numberEntries != 0)
			{
				for (int i = 0; i < numberEntries; i++)
				{
					myInventory[i] = new InventoryItem();
					myInventory[i].description = inputFile.readLine();
					myInventory[i].location = inputFile.readLine();
					myInventory[i].serialNumber = inputFile.readLine();
					myInventory[i].marked =Boolean.valueOf(inputFile.readLine()).booleanValue();
					myInventory[i].purchasePrice =inputFile.readLine();
					myInventory[i].purchaseDate = inputFile.readLine();
					myInventory[i].purchaseLocation =inputFile.readLine();
					myInventory[i].note = inputFile.readLine();
					myInventory[i].photoFile = inputFile.readLine();
				}
			}
			
			n = Integer.valueOf(inputFile.readLine()).intValue();
			if (n != 0)
			{
				for (int i = 0; i < n; i++)
				{
					locationComboBox.addItem(inputFile.readLine());
				}
			}
			
			inputFile.close();
			currentEntry = 1;
			showEntry(currentEntry);
		}
		catch (Exception ex)
		{
			numberEntries = 0;
			currentEntry = 0;
		}
		if (numberEntries == 0)
		{
			newButton.setEnabled(false);
			deleteButton.setEnabled(false);
			nextButton.setEnabled(false);
			previousButton.setEnabled(false);
			printButton.setEnabled(false);
		}	
	}			
			
	protected void noteTextFieldActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		photoButton.requestFocus();
	}

	protected void storeTextFieldActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		noteTextField.requestFocus();
	}

	protected void dateDateChooserPropertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		storeTextField.requestFocus();
	}

	protected void priceTextFieldActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		dateDateChooser.requestFocus();
	}

	protected void serialTextFieldActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		priceTextField.requestFocus();
	}

	protected void locationComboBoxActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (locationComboBox.getItemCount() != 0)
		{
			for (int i = 0; i < locationComboBox.getItemCount(); i++)
			{
				if(locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString()))
				{
					serialTextField.requestFocus();return;
				}
			}
		}
		locationComboBox.addItem(locationComboBox.getSelectedItem());
		serialTextField.requestFocus();
	}

	protected void itemTextFieldActionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		locationComboBox.requestFocus();
	}

	protected void searchButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int i;
		if (numberEntries == 0)
			return;
		
		String letterClicked = e.getActionCommand();
		i = 0;
		do
		{
			if (myInventory[i].description.substring(0, 1).equals(letterClicked))
			{
				currentEntry = i + 1;
				showEntry(currentEntry);
				return;
			}
			i++;
		}
		while (i < numberEntries);
		JOptionPane.showConfirmDialog(null, "No " + letterClicked + " inventory items.","None Found", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
	}

	protected void exitButtonActionPerformed() {
		// TODO Auto-generated method stub
		exitForm(null);
	}

	protected void printButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		lastPage = (int) (1 + (numberEntries - 1) / entriesPerPage);
		PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
		inventoryPrinterJob.setPrintable(new InventoryDocument());
		if (inventoryPrinterJob.printDialog())
		{
			try
			{
				inventoryPrinterJob.print();
			}
			catch (PrinterException ex)
			{
				JOptionPane.showConfirmDialog(null, ex.getMessage(), "Print Error",JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected void nextButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		checkSave();
		currentEntry++;
		showEntry(currentEntry);
	}

	private void checkSave() {
		// TODO Auto-generated method stub
		boolean edited = false;
		if (!myInventory[currentEntry - 1].description.equals(itemTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry -1].location.equals(locationComboBox.getSelectedItem().toString()))
			edited = true;
		else if (myInventory[currentEntry - 1].marked != markedCheckBox.isSelected())
			edited = true;
		else if (!myInventory[currentEntry - 1].serialNumber.equals(serialTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry -1].purchaseDate.equals(dateToString(dateDateChooser.getDate())))
			edited = true;
		else if (!myInventory[currentEntry -1].purchaseLocation.equals(storeTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText()))
			edited = true;
		else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText()))
			edited = true;
		
		if (edited)
		{
			if (JOptionPane.showConfirmDialog(null, "You have edited this item. Do you want tosave the changes?", "Save Item", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				saveButton.doClick();
		}
	}

	protected void previousButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		checkSave();
		currentEntry--;
		showEntry(currentEntry);
	}

	protected void saveButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		itemTextField.setText(itemTextField.getText().trim());
		if (itemTextField.getText().equals(""))
		{
			JOptionPane.showConfirmDialog(null, "Must have item description.", "Error",JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			itemTextField.requestFocus();
			return;
		}
		
		if (newButton.isEnabled())
		{
			deleteEntry(currentEntry);
		}
		String s = itemTextField.getText();
		itemTextField.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
		numberEntries++;
		
		currentEntry = 1;
		if (numberEntries != 1)
		{
			do
			{
				if(itemTextField.getText().compareTo(myInventory[currentEntry - 1].description) < 0)
					break;
				currentEntry++;
			}
			while (currentEntry < numberEntries);
		}
		
		if (currentEntry != numberEntries)
		{
			for (int i = numberEntries; i >= currentEntry + 1; i--)
			{
				myInventory[i - 1] = myInventory[i - 2];
				myInventory[i - 2] = new InventoryItem();
			}
		}
		myInventory[currentEntry - 1] = new InventoryItem();
		myInventory[currentEntry - 1].description = itemTextField.getText();
		myInventory[currentEntry - 1].location =locationComboBox.getSelectedItem().toString();
		myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
		myInventory[currentEntry - 1].serialNumber = serialTextField.getText();
		myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
		myInventory[currentEntry - 1].purchaseDate =dateToString(dateDateChooser.getDate());
		myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
		myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
		myInventory[currentEntry - 1].note = noteTextField.getText();
		showEntry(currentEntry);
		
		if (numberEntries < maximumEntries)
			newButton.setEnabled(true);
		else
			newButton.setEnabled(false);
		deleteButton.setEnabled(true);
		printButton.setEnabled(true);	
	}

	protected void deleteButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?","Delete Inventory Item", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return;
		
		deleteEntry(currentEntry);
		if (numberEntries == 0)
		{
			currentEntry = 0;
			blankValues();
		}
		else
		{
			currentEntry--;
			if (currentEntry == 0)
				currentEntry = 1;
			showEntry(currentEntry);
		}
	}

	protected void newButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		checkSave();
		blankValues();
	}

	protected void photoButtonActionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		JFileChooser openChooser = new JFileChooser();
		openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		openChooser.setDialogTitle("Open Photo File");
		openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files","jpg"));
		if (openChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			showPhoto(openChooser.getSelectedFile().toString());
	}

	protected void deleteEntry(int currentEntry2) {
		// TODO Auto-generated method stub
		if (j != numberEntries)
		{
			for (int i = j; i < numberEntries; i++)
			{
				myInventory[i - 1] = new InventoryItem();
				myInventory[i - 1] = myInventory[i];
			}
		}
		numberEntries--;
	}

	protected void exitForm(WindowEvent arg0) {
		// TODO Auto-generated method stub
		if (JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nAre yousure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return;
		
		try
		{
			PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("inventory.txt")));
			outputFile.println(numberEntries);
			if (numberEntries != 0)
			{
				for (int i = 0; i < numberEntries; i++)
				{
					outputFile.println(myInventory[i].description);
					outputFile.println(myInventory[i].location);
					outputFile.println(myInventory[i].serialNumber);
					outputFile.println(myInventory[i].marked);
					outputFile.println(myInventory[i].purchasePrice);
					outputFile.println(myInventory[i].purchaseDate);
					outputFile.println(myInventory[i].purchaseLocation);
					outputFile.println(myInventory[i].note);
					outputFile.println(myInventory[i].photoFile);
				}
			}
			
			outputFile.println(locationComboBox.getItemCount());
			if (locationComboBox.getItemCount() != 0)
			{
				for (int i = 0; i < locationComboBox.getItemCount(); i++)
					outputFile.println(locationComboBox.getItemAt(i));
			}
			outputFile.close();
		}
		catch (Exception ex)
		{
				
		}
		System.exit(0);
	}
	
	private void sizeButton(JButton b, Dimension bSize)
	{
		// TODO Auto-generated method stub
		b.setPreferredSize(bSize);
		b.setMinimumSize(bSize);
		b.setMaximumSize(bSize);
	}
	
	private void showEntry(int j){
		itemTextField.setText(myInventory[j - 1].description);
		locationComboBox.setSelectedItem(myInventory[j - 1].location);
		markedCheckBox.setSelected(myInventory[j - 1].marked);
		serialTextField.setText(myInventory[j - 1].serialNumber);
		priceTextField.setText(myInventory[j - 1].purchasePrice);
		dateDateChooser.setDate(stringToDate(myInventory[j - 1].purchaseDate));
		storeTextField.setText(myInventory[j - 1].purchaseLocation);
		noteTextField.setText(myInventory[j - 1].note);
		showPhoto(myInventory[j - 1].photoFile);
		nextButton.setEnabled(true);
		previousButton.setEnabled(true);
		if (j == 1)
			previousButton.setEnabled(false);
		if (j == numberEntries)
			nextButton.setEnabled(false);
		itemTextField.requestFocus();
	}

	private void showPhoto(String photoFile) {
		// TODO Auto-generated method stub
		if (!photoFile.equals(""))
		{
			try
			{
				photoTextArea.setText(photoFile);
			}
			catch (Exception ex)
			{
				photoTextArea.setText("");
			}
		}
		else
		{
			photoTextArea.setText("");
		}
		photoPanel.repaint();
	}

	private Date stringToDate(String s) {
		// TODO Auto-generated method stub
		int m = Integer.valueOf(s.substring(0, 2)).intValue() - 1;
		int d = Integer.valueOf(s.substring(3, 5)).intValue();
		int y = Integer.valueOf(s.substring(6)).intValue() - 1900;
		return(new Date(y, m, d));
	}
	
	private String dateToString(Date dd){
		String yString = String.valueOf(dd.getYear() + 1900);
		int m = dd.getMonth() + 1;
		String mString = new DecimalFormat("00").format(m);
		int d = dd.getDate();
		String dString = new DecimalFormat("00").format(d);
		return(mString + "/" + dString + "/" + yString);
	}
	
	private void blankValues()
	{
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
		saveButton.setEnabled(true);
		previousButton.setEnabled(false);
		nextButton.setEnabled(false);
		printButton.setEnabled(false);
		itemTextField.setText("");
		locationComboBox.setSelectedItem("");
		markedCheckBox.setSelected(false);
		serialTextField.setText("");
		priceTextField.setText("");
		dateDateChooser.setDate(new Date());
		storeTextField.setText("");
		noteTextField.setText("");
		photoTextArea.setText("");
		photoPanel.repaint();
		itemTextField.requestFocus();
	}
}

class PhotoPanel extends JPanel 
{
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g2d);
		g2d.setPaint(Color.BLACK);
		g2d.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
		Image photoImage = new ImageIcon(HomeInventory.photoTextArea.getText()).getImage( );
		int w = getWidth();
		int h = getHeight();
		double rWidth = (double) getWidth() / (double) photoImage.getWidth(null);
		double rHeight = (double) getHeight() / (double) photoImage.getHeight(null);
		if (rWidth > rHeight)
		{
			w = (int) (photoImage.getWidth(null) * rHeight);
		}
		else
		{
			h = (int) (photoImage.getHeight(null) * rWidth);
		}
		g2d.drawImage(photoImage, (int) (0.5 * (getWidth() - w)), (int) (0.5 * (getHeight() - h)),w, h, null);
		g2d.dispose();
	}
}
	
class InventoryDocument implements Printable	
{
	public int print(Graphics g, PageFormat pf, int pageIndex)
	{
		Graphics2D g2D = (Graphics2D) g;
		if ((pageIndex + 1) > HomeInventory.lastPage)
		{
			return NO_SUCH_PAGE;
		}
		
		int i, iEnd;
		
		g2D.setFont(new Font("Arial", Font.BOLD, 14));
		g2D.drawString("Home Inventory Items - Page " + String.valueOf(pageIndex + 1),(int) pf.getImageableX(), (int) (pf.getImageableY() + 25));
		
		int dy = (int) g2D.getFont().getStringBounds("S",g2D.getFontRenderContext()).getHeight();
		int y = (int) (pf.getImageableY() + 4 * dy);
		iEnd = HomeInventory.entriesPerPage * (pageIndex + 1);
		if (iEnd > HomeInventory.numberEntries)
			iEnd = HomeInventory.numberEntries;
		
		for (i = 0 + HomeInventory.entriesPerPage * pageIndex; i < iEnd; i++)
		{
			Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(), y,pf.getImageableX() + pf.getImageableWidth(), y);
			g2D.draw(dividingLine);
			
			y += dy;
			g2D.setFont(new Font("Arial", Font.BOLD, 12));
			g2D.drawString(HomeInventory.myInventory[i].description, (int) pf.getImageableX(), y);
			
			y += dy;
			g2D.setFont(new Font("Arial", Font.PLAIN, 12));
			g2D.drawString("Location: " + HomeInventory.myInventory[i].location, (int)(pf.getImageableX() + 25), y);
			
			y += dy;
			if (HomeInventory.myInventory[i].marked)
				g2D.drawString("Item is marked with identifying information.", (int)(pf.getImageableX() + 25), y);
			else
				g2D.drawString("Item is NOT marked with identifying information.", (int)(pf.getImageableX() + 25), y);
			
			y += dy;
			g2D.drawString("Serial Number: " +HomeInventory.myInventory[i].serialNumber, (int) (pf.getImageableX() + 25), y);
			
			y += dy;
			g2D.drawString("Price: $" + HomeInventory.myInventory[i].purchasePrice + ",Purchased on: " + HomeInventory.myInventory[i].purchaseDate, (int) (pf.getImageableX() +25), y);
			
			y += dy;
			g2D.drawString("Purchased at: " +HomeInventory.myInventory[i].purchaseLocation, (int) (pf.getImageableX() + 25), y);
			
			y += dy;
			g2D.drawString("Note: " + HomeInventory.myInventory[i].note, (int)(pf.getImageableX() + 25), y);
			
			y += dy;
			try
			{
				Image inventoryImage = new ImageIcon(HomeInventory.myInventory[i].photoFile).getImage ();
				double ratio = (double) (inventoryImage.getWidth(null)) / (double)inventoryImage.getHeight(null);
				g2D.drawImage(inventoryImage, (int) (pf.getImageableX() + 25), y, (int) (100 *ratio), 100, null);
			}
			catch(Exception ex)
			{
				
			}
			y += 2 * dy + 100;
		}
		return PAGE_EXISTS;
	}
}
