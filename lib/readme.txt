Please Note: As Infonode Docking Window cannot be found in any public maven repositories there's no way to use it without installing it on a local Maven repo manually. To install the library please use the following command:

mvn install:install-file -Dfile=<path-to-file> -DgroupId=com.infonode -DartifactId=idw-gpl -Dversion=1.6.1 -Dpackaging=jar