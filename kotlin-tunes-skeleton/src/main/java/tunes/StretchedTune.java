package tunes;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

class StretchedTune implements Tune {

    private Tune targetTune;
    private double stretchFactor;

    public StretchedTune(Tune targetTune, double stretchFactor) {
        this.targetTune = targetTune;
        this.stretchFactor = stretchFactor;
    }

    @NotNull
    @Override
    public List<Note> getNotes() {
        return targetTune.getNotes().stream().map(new Function<Note, Note>() {
            @Override
            public Note apply(Note note) {
                return new Note(note.getPitch(), Math.min(note.getDuration() * stretchFactor, 64.0));
            }
        }).toList();
    }

    @Override
    public double getTotalDuration() {
        return this.getNotes().stream().mapToDouble(new ToDoubleFunction<Note>() {
            @Override
            public double applyAsDouble(Note note) {
                return note.getDuration();
            }
        }).sum();
    }

    @Override
    public void addNote(@NotNull Note note) {
        this.targetTune.addNote(new Note(note.getPitch(), Math.min(note.getDuration() / stretchFactor, 64.0)));
    }

    @NotNull
    @Override
    public Iterator<Note> iterator() {
        return this.getNotes().iterator();
    }
}
