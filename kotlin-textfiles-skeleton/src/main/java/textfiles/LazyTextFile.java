package textfiles;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public final class LazyTextFile implements TextFile {

    private TextFile targetTextFile;
    private Pair<Integer, String> pendingInsertion = null;

    public LazyTextFile(TextFile targetTextFile) {
        this.targetTextFile = targetTextFile;
    }

    @Override
    public int getLength() {
        this.flush();
        return targetTextFile.getLength();
    }

    private void flush() {
        if (pendingInsertion != null) {
            targetTextFile.insertText(pendingInsertion.getFirst(), pendingInsertion.getSecond());
            pendingInsertion = null;
        }
    }

    @Override
    public void insertText(int offset, @NotNull String toInsert) {
       if(pendingInsertion!=null) {
           if (Objects.equals(offset, pendingInsertion.getFirst())) {
               pendingInsertion=new Pair<Integer,String>(offset, toInsert+pendingInsertion.getSecond());
               return;
           }
           else {
               flush();
           }
       }
       pendingInsertion=new Pair<Integer,String>(offset,toInsert);
    }

    @Override
    public void deleteText(int offset, int size) {
        if (offset < 0 || size < 0)    throw new FileIndexOutOfBoundsException();
        this.flush();
        targetTextFile.deleteText(offset,size);
    }

    @Override
    public String toString() {
        this.flush();
        return targetTextFile.toString();
    }

    @Override
    public int compareTo(@NotNull TextFile other) {
        return this.toString().compareTo(other.toString());
    }
}
