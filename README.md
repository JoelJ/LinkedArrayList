LinkedArrayList
===============

This is a programming exercise I did. I'm not sure if there are already implementations of this list out there, but I don't really care as I was doing this more for the challenge and less because I thought it'd be useful.
However, I wrote it as if I were going to use it. So I have tests making sure it does what I expect it to do.
A List that is essentially backed by a LinkedList of arrays. Insertion is quick, indexing isn't horrible, and removal is pretty good.

When the list is created a "block size" is given. This is the size of each LinkedList node, or as the code calls it, a 'Block.
When a block fills up, a new one is created and the old one points to it as the next block of data.
This makes expansion nearly instant.

When an item is removed, only the block it was contained in needs to be adjusted.
So, for example, if the block size is 10, and the list has 1000 elements.
If element at index 51 is removed, only the following 8 items in that block need to be moved down to fill in that space.
