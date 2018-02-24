CREATE TABLE IF NOT EXISTS 
        main.users (
          id INTEGER PRIMARY KEY NOT NULL UNIQUE,
          username TEXT NOT NULL, 
          password TEXT NOT NULL, 
          email TEXT NOT NULL, 
          firstname TEXT NOT NULL,
          lastname TEXT NOT NULL, 
          gender TEXT NOT NULL,
          personid TEXT NOT NULL, 
          info TEXT NOT NULL
                   );

CREATE TABLE IF NOT EXISTS 
        main.authKeys (
          key TEXT PRIMARY KEY NOT NULL UNIQUE,
          userid TEXT NOT NULL,
          time DATETIME NOT NULL
                   );
                   
CREATE TABLE IF NOT EXISTS 
        main.persons (
          id TEXT PRIMARY KEY NOT NULL UNIQUE,
          userid TEXT NOT NULL,
          firstname TEXT NOT NULL,
          lastname TEXT NOT NULL,
          gender TEXT NOT NULL,
          father TEXT,
          mother TEXT,
          spouse TEXT
                      );


CREATE TABLE IF NOT EXISTS 
        main.events (
          id TEXT PRIMARY KEY NOT NULL UNIQUE,
          userid TEXT NOT NULL,
          personid TEXT NOT NULL,
          latitude REAL NOT NULL,
          longitude REAL NOT NULL,
          country TEXT NOT NULL,
          city TEXT NOT NULL,
          eventType TEXT NOT NULL,
          year INTEGER NOT NULL
                   );

