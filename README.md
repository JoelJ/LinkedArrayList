=LinkedArrayList=

This is a programming exercise I did. I'm not sure if there are already implementations of this list out there, but I don't really care as I was doing this more for the challenge and less because I thought it'd be useful.
However, I wrote it as if I were going to use it. So I have tests making sure it does what I expect it to do.
A List that is essentially backed by a LinkedList of arrays. Insertion is quick, indexing isn't horrible, and removal is pretty good.

==Blocks==
When the list is created a "block size" is given. This is the size of each LinkedList node, or as the code calls it, a 'Block'.
When a block fills up, a new one is created and the old one points to it as the next block of data.
This makes expansion nearly instant.

==Removal==
When an item is removed, only the block it was contained in needs to be adjusted.
So, for example, if the block size is 10, and the list has 1000 elements.
If element at index 51 is removed, only the following 8 items in that block need to be moved down to fill in that space. The other 990 elements don't even need to know something happened. 

The larger the block size, the worse the removal performs. Because it has to move that many more elements on every removal.

==Indexing==
Random indexing is only a little better than a singly-linked list.
Rather than iterating every element to find the specified index, the blocks are iterated.
As it's iterating the blocks, it keeps track of how many elements it's skipping so it knows which block to stop at.
When it finds the block that contains the given index, it just indexes directly into that block and returns the value.

The larger the block size, the better the indexing performs, since it can skip that many more elements as it traverses the blocks and once it has the block that contains the index, it does an array index to get the value.

