#!/bin/bash
cd cls
#java SylWord ../testdata/welsh/wel_vowels.txt ../testdata/welsh/wel_text1.txt
java SylWord ../testdata/full/vowels.txt ../testdata/full/in.txt /tmp/fulltest_out.txt
#vimdiff ../testdata/full/ideal_output.txt /tmp/fulltest_out.txt
