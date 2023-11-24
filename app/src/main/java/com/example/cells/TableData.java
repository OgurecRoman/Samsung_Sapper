package com.example.cells;

public class TableData{
    public final int RowIndex;
    public final int ColumnIndex;
    public final int ImageId;

    public TableData(int rowIndex, int columnIndex, int imageId) {
        RowIndex = rowIndex;
        ColumnIndex = columnIndex;
        ImageId = imageId;
    }
}