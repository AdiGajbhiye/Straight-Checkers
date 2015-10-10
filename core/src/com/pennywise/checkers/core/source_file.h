//
// Created by CHOXXY on 10/10/2015.
//

#ifndef FINDWORDS_SOURCE_FILE_H
#define FINDWORDS_SOURCE_FILE_H




int value[17] = {0, 0, 0, 0, 0, 1, 256, 0, 0, 16, 4096, 0, 0, 0, 0, 0, 0};
int *play;

/* required functions */
int getmove(int b[8][8], int color, double maxtime, char str[255], int *playnow, int info,
                  int unused, struct CBmove *move);


void movetonotation(struct move2 move, char str[80]);

/*----------> part II: search */
int checkers(int b[46], int color, double maxtime, char *str);
int alphabeta(int b[46], int depth, int alpha, int beta, int color);
int firstalphabeta(int b[46], int depth, int alpha, int beta, int color,
                   struct move2 *best);
void domove(int b[46], struct move2 move);
void undomove(int b[46], struct move2 move);
int evaluation(int b[46], int color);

/*----------> part III: move generation */
int generatemovelist(int b[46], struct move2 movelist[MAXMOVES], int color);
int generatecapturelist(int b[46], struct move2 movelist[MAXMOVES], int color);
void blackmancapture(int b[46], int *n, struct move2 movelist[MAXMOVES], int square);
void blackkingcapture(int b[46], int *n, struct move2 movelist[MAXMOVES], int square);
void whitemancapture(int b[46], int *n, struct move2 movelist[MAXMOVES], int square);
void whitekingcapture(int b[46], int *n, struct move2 movelist[MAXMOVES], int square);
int testcapture(int b[46], int color);

#endif //FINDWORDS_SOURCE_FILE_H
