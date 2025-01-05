package org.example.todo;

import org.example.todo.model.Note;
import org.example.todo.repository.NoteRepository;
import org.example.todo.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteService noteService;
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        // Створюємо mock NoteRepository
        noteRepository = Mockito.mock(NoteRepository.class);
        noteService = new NoteService(noteRepository);
    }

    @Test
    void testAddNote() {
        Note note = new Note();
        note.setTitle("Test Note");
        note.setContent("This is a test note.");

        when(noteRepository.save(note)).thenReturn(note);

        Note addedNote = noteService.add(note);

        assertNotNull(addedNote);
        assertEquals("Test Note", addedNote.getTitle());
        assertEquals("This is a test note.", addedNote.getContent());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testListAllNotes() {
        Note note1 = new Note();
        note1.setTitle("Note 1");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setTitle("Note 2");
        note2.setContent("Content 2");

        when(noteRepository.findAll()).thenReturn(List.of(note1, note2));

        List<Note> notes = noteService.listAll();

        assertEquals(2, notes.size());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void testDeleteNoteById() {
        long noteId = 1L;
        doNothing().when(noteRepository).deleteById(noteId);

        noteService.deleteById(noteId);

        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    void testUpdateNote() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Original Title");
        note.setContent("Original Content");

        when(noteRepository.save(note)).thenReturn(note);

        noteService.update(note);

        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void testGetNoteById() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Sample Note");
        note.setContent("Sample Content");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        Note fetchedNote = noteService.getById(1L);

        assertNotNull(fetchedNote);
        assertEquals("Sample Note", fetchedNote.getTitle());
        assertEquals("Sample Content", fetchedNote.getContent());
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetNoteByIdThrowsException() {
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> noteService.getById(999L));
        verify(noteRepository, times(1)).findById(999L);
    }
}
