package com.cornez.shades;

class RecordTable {

    //MEMBER ATTRIBUTES
    private int _id;
    private String name;
    private int time;

    public RecordTable() {
    }

    public RecordTable(int id, String n, int t) {
        _id = id;
        name = n;
        time = t;
    }

    public RecordTable(RecordTable recordTable) {
        _id = recordTable.getId();
        name = recordTable.getName();
        time = recordTable.getTime();
    }

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }

    public String getName () {
        return name;
    }
    public void setName (String n) { name = n; }

    public int getTime() {
        return time;
    }
    public void setTime(int t) { time = t; }

}
