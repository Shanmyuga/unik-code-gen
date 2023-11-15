/**
 * 
 */
package com.cognizant.fecodegen.bo;

/**
 * @author 238209
 *
 */
public class ChildComponent {

	private int rowNumber;
	
	private int columnNumber;
	
	private int xs = 12;
	
	private int xsOffset;
	
	private int md;
	
	private int mdOffset;
	
	private String elemContent;
	
	private double colWidth;

	public ChildComponent(String elemContent) {
		this.elemContent = elemContent;
	}
	
	public ChildComponent(String elemContent, int rowNumber, int columnNumber) {
		this.elemContent = elemContent;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
	}
	
	/**
	 * @return the rowNumber
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * @param rowNumber the rowNumber to set
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * @return the columnNumber
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * @param columnNumber the columnNumber to set
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * @return the elemContent
	 */
	public String getElemContent() {
		return elemContent;
	}

	/**
	 * @param elemContent the elemContent to set
	 */
	public void setElemContent(String elemContent) {
		this.elemContent = elemContent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return elemContent;
	}

	/**
	 * @return the xs
	 */
	public int getXs() {
		return xs;
	}

	/**
	 * @param xs the xs to set
	 */
	public void setXs(int xs) {
		this.xs = xs;
	}

	/**
	 * @return the md
	 */
	public int getMd() {
		return md;
	}

	/**
	 * @param md the md to set
	 */
	public void setMd(int md) {
		this.md = md;
	}

	/**
	 * @return the xsOffset
	 */
	public int getXsOffset() {
		return xsOffset;
	}

	/**
	 * @param xsOffset the xsOffset to set
	 */
	public void setXsOffset(int xsOffset) {
		this.xsOffset = xsOffset;
	}

	/**
	 * @return the mdOffset
	 */
	public int getMdOffset() {
		return mdOffset;
	}

	/**
	 * @param mdOffset the mdOffset to set
	 */
	public void setMdOffset(int mdOffset) {
		this.mdOffset = mdOffset;
	}

	/**
	 * @return the colWidth
	 */
	public double getColWidth() {
		return colWidth;
	}

	/**
	 * @param colWidth the colWidth to set
	 */
	public void setColWidth(double colWidth) {
		this.colWidth = colWidth;
	}
	
}
