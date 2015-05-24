/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven.resutil;

import haven.*;

import java.util.ArrayList;
import java.util.Random;

public class GaussianPlant extends CSprite {
    protected GaussianPlant(Owner owner, Resource res) {
        super(owner, res);
    }

    public static class Factory implements Sprite.Factory {
        private static final Tex[] typebarda = new Tex[0];
        public Tex[] strands;
        public int num;
        public Resource.Neg neg;

        public Factory(int num) {
            Resource res = Utils.myres(this.getClass());
            this.neg = res.layer(Resource.negc);
            this.num = num;
            ArrayList<Tex> strands = new ArrayList<Tex>();
            for (Resource.Image img : res.layers(Resource.imgc)) {
                if (img.id != -1)
                    strands.add(img.tex());
            }
            this.strands = strands.toArray(typebarda);
        }

        public Sprite create(Owner owner, Resource res, Message sdt) {
            GaussianPlant spr = new GaussianPlant(owner, res);
            spr.addnegative();
            Random rnd = owner.mkrandoom();
            Coord bs = neg.bs;
            for (int i = 0; i < num; i++) {
                Coord c = new Coord((int) (rnd.nextGaussian() * bs.x / 2), (int) (rnd.nextGaussian() * bs.y / 2));
                Tex s = strands[rnd.nextInt(strands.length)];
                spr.add(s, 0, MapView.m2s(c), new Coord(s.sz().x / 2, s.sz().y).inv());
            }
            return (spr);
        }
    }
}