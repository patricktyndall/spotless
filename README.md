##spotless

#What?
A quick and dirty little tool for creating and (quickly) adding tracks to new playlists on Spotify.

#Why?
The Spotify workflow for adding tracks to playlists is pretty clunky--you need to search, load the results, right-click on 
the correct one, and select the target playlist from the menu. This can be a serious pain if you need to add a bunch of tracks
at once. 

#Who?
This is designed for that critical musical member of any social group, he who find himself frequently creating new playlists 
for barbecues, parties, and road trips simply because nobody else has the musical chops. If you've already got the tracks
in mind, and don't need a page of results to sift through, this will let you make a playlist in a fraction of the time.

##fun implementation details
- links directly into your Spotify account to get info about your playlists and to allow you to edit them from within the program
- multithreaded searching and loading of results and album art for quick response time
- authentication happens by redirecting an HTTP request to the local machine and parsing to get OAuth 2 credentials
- support coming to add playlists from text files, generate playlists from friends' lists and more
