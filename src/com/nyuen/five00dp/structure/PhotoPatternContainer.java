package com.nyuen.five00dp.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.nyuen.five00dp.R;

import android.widget.RelativeLayout;

public class PhotoPatternContainer {

    public enum Pattern {
        //First int = # of photos that a pattern can hold
        //Rest of the in = size of the image and should be loaded for each grid
        ONE(1, 4, 0, 0, 0),
        TWO_VERTICAL(2, 3, 3, 0, 0),
        TWO_HORIZONTAL(2, 3, 3, 0, 0),
        FOUR(4, 3, 3, 3, 3),
        FOUR_VERTICAL(4, 3, 3, 3, 3),
        THREE_AAB(3, 3, 3, 3, 0),
        THREE_ABA(3, 3, 3, 3, 0),
        THREE_BAA(3, 3, 3, 3, 0),
        THREE_VERTICAL(3, 3, 3, 3, 0),
        THREE_HORIZONTAL(3, 3, 3, 3, 0);

        private final int mImageCount;
        private final int[] mImageSizes;

        public int getCount() { return mImageCount; }

        Pattern(int i, int... sizes) {
            mImageCount = i;
            mImageSizes = sizes;
        }

        public int[] getSizes() {
            return mImageSizes;
        }

        public RelativeLayout.LayoutParams[] getParams(int width, int margin) {
            int WIDTH_ONE = width;
            int HEIGHT_ONE = width*2/3;
            int WIDTH_HALF = WIDTH_ONE / 2;
            int HEIGHT_HALF = HEIGHT_ONE / 2;
            int WIDTH_QUATER = WIDTH_ONE / 4;
            int MARGIN_ONE = margin;
            int MARGIN_TWO = MARGIN_ONE + MARGIN_ONE;
            int MARGIN_HALF = MARGIN_ONE / 2;
            int MARGIN_QUATER = MARGIN_ONE / 4;
            int MARGIN_THREE_HALF = MARGIN_HALF * 3;

            RelativeLayout.LayoutParams[] params = new RelativeLayout.LayoutParams[this.mImageCount];

            if(this.equals(Pattern.ONE)) {
                params[0] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, HEIGHT_ONE - MARGIN_TWO);
                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, 0);
            }else if(this.equals(Pattern.TWO_VERTICAL)) {
                params[0] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_ONE - MARGIN_TWO);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_ONE - MARGIN_TWO);

                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, 0);
                params[1].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, 0);

                params[1].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
            }else if(this.equals(Pattern.TWO_HORIZONTAL)) {
                // Pattern.TWO_HORIZONTAL
                params[0] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, HEIGHT_HALF - MARGIN_THREE_HALF);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, HEIGHT_HALF - MARGIN_THREE_HALF);

                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
                params[1].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_ONE, 0);

                params[1].addRule(RelativeLayout.BELOW, R.id.imageView0);
            }else if(this.equals(Pattern.THREE_AAB)) {                
                params[0] = new RelativeLayout.LayoutParams(WIDTH_QUATER - MARGIN_THREE_HALF + MARGIN_QUATER, HEIGHT_ONE - MARGIN_TWO);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_QUATER - MARGIN_THREE_HALF + MARGIN_QUATER, HEIGHT_ONE - MARGIN_TWO);
                params[2] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_ONE - MARGIN_TWO);

                //left top right bottom
                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[1].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[2].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_ONE);

                params[1].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                params[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);           
            }else if(this.equals(Pattern.THREE_ABA)) {                
                params[0] = new RelativeLayout.LayoutParams(WIDTH_QUATER - MARGIN_THREE_HALF + MARGIN_QUATER, HEIGHT_ONE - MARGIN_TWO);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_ONE - MARGIN_TWO);
                params[2] = new RelativeLayout.LayoutParams(WIDTH_QUATER - MARGIN_THREE_HALF + MARGIN_QUATER, HEIGHT_ONE - MARGIN_TWO);

                //left top right bottom
                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[1].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[2].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_ONE);

                params[1].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                params[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);           
            }else if(this.equals(Pattern.THREE_VERTICAL)) {
                // Pattern.THREE_VERTICAL
                params[0] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_ONE - MARGIN_TWO);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_THREE_HALF);
                params[2] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_THREE_HALF);

                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, 0);
                params[1].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
                params[2].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

                params[1].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                params[2].addRule(RelativeLayout.BELOW, R.id.imageView1);
                params[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
            }else if(this.equals(Pattern.THREE_HORIZONTAL)) {
                // Pattern.THREE_HORIZONTAL
                params[0] = new RelativeLayout.LayoutParams(WIDTH_ONE - MARGIN_TWO, HEIGHT_HALF - MARGIN_THREE_HALF);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_THREE_HALF);
                params[2] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_THREE_HALF);

                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
                params[1].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_HALF, 0);
                params[2].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

                params[1].addRule(RelativeLayout.BELOW, R.id.imageView0);
                params[2].addRule(RelativeLayout.BELOW, R.id.imageView0);
                params[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);
            }else if(this.equals(Pattern.FOUR)) {
                // Pattern.FOUR
                params[0] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_TWO);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_TWO);
                params[2] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_TWO);
                params[3] = new RelativeLayout.LayoutParams(WIDTH_HALF - MARGIN_THREE_HALF, HEIGHT_HALF - MARGIN_TWO);

                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, MARGIN_HALF);
                params[1].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_HALF);
                params[2].setMargins(MARGIN_ONE, MARGIN_HALF, MARGIN_HALF, 0);
                params[3].setMargins(MARGIN_HALF, MARGIN_HALF, MARGIN_ONE, 0);

                params[1].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                params[2].addRule(RelativeLayout.BELOW, R.id.imageView0);
                params[3].addRule(RelativeLayout.BELOW, R.id.imageView1);
                params[3].addRule(RelativeLayout.RIGHT_OF, R.id.imageView2);
            } else if(this.equals(Pattern.FOUR_VERTICAL)) {
                // Pattern.FOUR_VERTICAL
                params[0] = new RelativeLayout.LayoutParams(WIDTH_HALF/2 - MARGIN_THREE_HALF + MARGIN_ONE/4, HEIGHT_ONE - MARGIN_TWO);
                params[1] = new RelativeLayout.LayoutParams(WIDTH_HALF/2 - MARGIN_THREE_HALF + MARGIN_ONE/4, HEIGHT_ONE - MARGIN_TWO);
                params[2] = new RelativeLayout.LayoutParams(WIDTH_HALF/2 - MARGIN_THREE_HALF + MARGIN_ONE/4, HEIGHT_ONE - MARGIN_TWO);
                params[3] = new RelativeLayout.LayoutParams(WIDTH_HALF/2 - MARGIN_THREE_HALF + MARGIN_ONE/4, HEIGHT_ONE - MARGIN_TWO);

                //left top right bottom
                params[0].setMargins(MARGIN_ONE, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[1].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[2].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_HALF, MARGIN_ONE);
                params[3].setMargins(MARGIN_HALF, MARGIN_ONE, MARGIN_ONE, MARGIN_ONE);

                params[1].addRule(RelativeLayout.RIGHT_OF, R.id.imageView0);
                params[2].addRule(RelativeLayout.RIGHT_OF, R.id.imageView1);
                params[3].addRule(RelativeLayout.RIGHT_OF, R.id.imageView2);
            }

            return params;
        }

        public static List<Pattern> getPatternList() {
            List<Pattern> randomList = new ArrayList<Pattern>();            
            randomList.add(Pattern.ONE);
            randomList.add(Pattern.TWO_VERTICAL);
            randomList.add(Pattern.TWO_HORIZONTAL);
            randomList.add(Pattern.FOUR);
            randomList.add(Pattern.FOUR_VERTICAL);
            randomList.add(Pattern.THREE_AAB);
            randomList.add(Pattern.THREE_ABA);
            randomList.add(Pattern.THREE_VERTICAL);
            randomList.add(Pattern.THREE_HORIZONTAL);
            Collections.shuffle(randomList);
            return randomList;
        }
    }

    private Pattern mPattern;
    private List<Integer> mPhotosIdx;

    public PhotoPatternContainer(Pattern p) {
        setPattern(p);
        mPhotosIdx = new ArrayList<Integer>();
    }

    public Pattern getPattern() {
        return mPattern;
    }

    public void setPattern(Pattern mPattern) {
        this.mPattern = mPattern;
    }

    public List<Integer> getPhotosIdx() {
        return mPhotosIdx;
    }

    public void setPhotos(List<Integer> list) {
        this.mPhotosIdx = list;
    }

    public void addPhotoID(int i) {
        mPhotosIdx.add(i);
    }


}
