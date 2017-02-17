# PairTreeZip
Serve a PairTree (https://wiki.ucop.edu/display/Curation/PairTree) and get volumes as zip files, mets.xml and metadata jsons.

# Build

To generate an executable package:

```
$ ./gradlew distZip
```

then find the result in ```build/distributions/pairtree-zip.zip```



# Use

## To get Volume zips when the volume list is unclean 
```
pairtree-zip/bin/pairtree-zip -pr <pairtree_root_path> -vl <volume_list_path> -dp <destination_path> 
```

## To get Volume zips when the volume list is clean
```
pairtree-zip/bin/pairtree-zip -pr <pairtree_root_path> -vl <volume_list_path> -dp <destination_path> -ci
```

## To get Volume (zip + mets.xml + json) when the volume list is clean
```
pairtree-zip/bin/pairtree-zip -pr <pairtree_root_path> -vl <volume_list_path> -dp <destination_path> -ci -mj
```
