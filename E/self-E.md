## Self-Evaluation Form for TAHBPL/E

A fundamental guideline of Fundamentals I and II is "one task, one
function" or, more generally, separate distinct tasks into distinct
program units. Even exploratory code benefits from this much proper
program design. 

This assignment comes with three distinct, unrelated tasks.

So, indicate below each bullet which file/unit takes care of each task:


1. dealing with the command-line argument (PORT)

https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/07e4845d2edc417fb13365dd68589f2e607a72ed/E/Other/xtcp/src/main/java/xtcp/XTcp.java#L12

This was arbitrary within java so it was not factored out into any other
methods. As we were only expecting a single argument, we simply accessed args[0]
and passed it on as the port when creating a Server Socket.

2. connecting the client on the specified port to the functionality

https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/07e4845d2edc417fb13365dd68589f2e607a72ed/E/Other/xtcp/src/main/java/xtcp/XTcp.java#L14-L21

The set of lines given in the above permalink is the section of the main function
that accepts the client, reads the input from the client, passes the input on to the
core functionality and then writes back to the client with the processed input from
the core functionality. These steps are all done with calls to different functions.
Line 18 calls the function where the core functionality is stored.

3. core functionality (either copied or imported from `C`)

https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/07e4845d2edc417fb13365dd68589f2e607a72ed/E/Other/xtcp/src/main/java/xtcp/XJson.java#L22-L49

This core functionality was largely copied from 'C', but it had to be modified
slightly to be easily callable while still operating on its own through its main function.
The function is set up as a static function and takes in a JSON object and returns
a JSON array.


The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request. If you did *not* factor
out these pieces of functionality into separate functions/methods, say
so.

