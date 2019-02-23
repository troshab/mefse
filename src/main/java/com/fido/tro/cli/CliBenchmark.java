package com.fido.tro.cli;

import com.fido.tro.Mefse;
import org.apache.log4j.Logger;

/** Class to measure execution time */
public class CliBenchmark {

    /** Local logger where time info will write (in debug) */
    private final static Logger LOGGER = Logger.getLogger(Mefse.class);

    /** Time when benchmark begin */
    private final long beginTime;

    /** Initial constructor which set beginTime */
    public CliBenchmark() {
        beginTime = System.currentTimeMillis();;
    }

    /**
     * Method that calculate time difference
     * (write to debug log)
     *
     * @return long timeTaken
     */
    public long timeTaken() {
        final long endTime = System.currentTimeMillis();
        final long timeTaken = endTime - beginTime;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Total execution time: " + timeTaken + "ms");
        }

        return timeTaken;
    }
}
