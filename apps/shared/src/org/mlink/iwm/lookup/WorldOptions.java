package org.mlink.iwm.lookup;

import java.util.List;
import java.util.ArrayList;

/**
 * User: andrei
 * Date: Oct 24, 2006
    /**
     * Helps to support options for multiple worlds. World is a database schema. There coulde be more than one
     * world for application instance. Opion values may vary btw worlds
     */
public class WorldOptions{
        private String worldName;
        private  List<OptionItem> options;
        public boolean isLoaded;

        public WorldOptions(String worldName) {
            this.worldName = worldName;
            options = new ArrayList<OptionItem>();
            isLoaded=false;
        }

        public void addOption(OptionItem item){
            options.add(item);
        }

        public List<OptionItem> getOptions() {
            return options;
        }

        public String getWorldName() {
            return worldName;
        }

        public void reset(){
            options = new ArrayList<OptionItem>();
        }

    }
