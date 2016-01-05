Anacleto project

Directory structure:

src- API source files

adminsite - Anacleto administration user interface

build - Output files

buildscript      - Schemas for the build process

lib/antlibs      - Ant build process depends on these projects
lib/dependencies - Anacleto depends on these projects, 
				   include these jars eather in the build process, 
				   as as in the final distribution
lib/j2eelibs     - needed for the build process, but they will not be deployed
lib/junit        - Junit libraries, needed for the build process, no deploy

templates        - Anacleto 'factory' templates


