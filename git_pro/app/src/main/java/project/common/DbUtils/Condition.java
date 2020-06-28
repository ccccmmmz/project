package project.common.DbUtils;

import android.content.ContentValues;

import java.util.Set;

public class Condition {
    private ContentValues contentValues;
    private StringBuilder stringBuffer;
    private String[] selectionArgs ;
    private int index = 0;
    public Condition(ContentValues contentValues) {
        if (contentValues==null) {
            return;
        }
        this.contentValues = contentValues;
        stringBuffer = new StringBuilder();
        selectionArgs = new String[contentValues.size()];
        Set<String> strings = contentValues.keySet();

        for (String string : strings) {
            stringBuffer.append(string);
            stringBuffer.append(" = ?");
            if (index!=strings.size()-1) {
                stringBuffer.append(" and ");
            }
            selectionArgs[index] = contentValues.getAsString(string);
            index++;
        }

    }

    public String getSelection() {
        return stringBuffer.length()==0?null:stringBuffer.toString();
    }

    public String[] getSelectionArgs() {
        return selectionArgs.length==0?null:selectionArgs;
    }
}
