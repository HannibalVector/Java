package hr.fer.zemris.java.hw13;

import javax.servlet.ServletContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Embeds collection of all artists which can be chosen from in a poll.
 * Collection of artists are read from file and current poll results
 * are cached in file automatically.
 * @author ilijan
 */
public class Artists {
    /** List of artists. */
    private static Optional<List<ArtistEntry>> list = Optional.empty();
    /** List of artist ratings in a poll. */
    private static Optional<List<Integer>> results = Optional.empty();
    /** Servlet context used to determine absolute path to the files. */
    private static ServletContext servletContext;

    /**
     * Implements artist entry in the list of artists.
     */
    public static class ArtistEntry {
        /** Artist name. */
        private String name;
        /** Hyperlink to one popular song of the artist. */
        private String linkToSong;
        /** Current number of votes in a poll. */
        private int numberOfVotes;

        /**
         * Constructor initializes new {@link ArtistEntry} using artist name and link to one
         * popular song.
         * @param name          name of the artist.
         * @param linkToSong    link to one popular song.
         */
        public ArtistEntry(String name, String linkToSong) {
            this.name = name;
            this.linkToSong = linkToSong;
        }

        /**
         * Getter for the artist name.
         * @return  artist name.
         */
        public String getName() {
            return name;
        }

        /**
         * Getter for the link to the one popular song.
         * @return  link to the song.
         */
        public String getLinkToSong() {
            return linkToSong;
        }

        /**
         * Upvotes artist by one in a poll.
         * @throws IOException  if writing poll results to a file is not possible.
         */
        public void vote() throws IOException {
            numberOfVotes++;
            saveResults();
        }

        /**
         * Gets current votes count in a poll.
         * @return  votes count in a poll.
         */
        public int getVotes() {
            return numberOfVotes;
        }
    }

    /**
     * Returns list of all artists.
     * @param servletContext    used to determine absolute path to a file containing collection of artists.
     * @return                  list of {@link hr.fer.zemris.java.hw13.Artists.ArtistEntry} objects.
     * @throws IOException      if reading from files containing artists or poll results is not possible.
     */
    public static List<ArtistEntry> getList(ServletContext servletContext) throws IOException {
        Artists.servletContext = servletContext;
        if (!list.isPresent()) {
            String fileName = servletContext.getRealPath("/WEB-INF/glasanje-definicija.txt");
            List<String> allLines = Files.readAllLines(Paths.get(fileName));
            list = Optional.of(new ArrayList<>());
            for (String line : allLines) {
                String[] entry = line.split("\\t");
                Artists.list.get().add(new Artists.ArtistEntry(entry[1], entry[2]));
            }
        }
        try {
            results = Optional.of(getResults(servletContext));

            for (int i = 0; i < results.get().size(); i++) {
                list.get().get(i).numberOfVotes = results.get().get(i);
            }
        } catch (Exception ignorable) {}

        return list.get();
    }

    /**
     * Saves poll results in a file.
     * @throws IOException  if writing to the file is not possible.
     */
    public static void saveResults() throws IOException {
        String fileName = servletContext.getRealPath("/WEB-INF/glasanje-rezultati.txt");
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE);
        int id = 1;
        for (Artists.ArtistEntry entry : Artists.list.get()) {
            writer.write(id + "\t" + entry.getVotes() + "\n");
            id++;
        }
        writer.close();
    }

    /**
     * Reads list of poll results from the file.
     * @param servletContext    used to determine absolute path to the file.
     * @return                  list of poll results.
     * @throws IOException      if reading from file is not possible.
     */
    private static List<Integer> getResults(ServletContext servletContext) throws IOException {
        String fileName = servletContext.getRealPath("/WEB-INF/glasanje-rezultati.txt");
        List<String> allLines = Files.readAllLines(Paths.get(fileName));
        List<Integer> results = new ArrayList<>();
        for (String line : allLines) {
            String[] entry = line.split("\\t");
            results.add(Integer.valueOf(entry[1]));
        }

        return results;
    }
}
