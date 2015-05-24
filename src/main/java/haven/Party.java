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

package haven;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Party {
    public static final int PD_LIST = 0;
    public static final int PD_LEADER = 1;
    public static final int PD_MEMBER = 2;
    Map<Integer, Member> memb = new TreeMap<Integer, Member>();
    Member leader = null;
    private Glob glob;

    public Party(Glob glob) {
        this.glob = glob;
    }

    public void msg(Message msg) {
        while (!msg.eom()) {
            int type = msg.uint8();
            if (type == PD_LIST) {
                ArrayList<Integer> ids = new ArrayList<Integer>();
                while (true) {
                    int id = msg.int32();
                    if (id < 0)
                        break;
                    ids.add(id);
                }
                Map<Integer, Member> nmemb = new TreeMap<Integer, Member>();
                for (int id : ids) {
                    Member m = memb.get(id);
                    if (m == null) {
                        m = new Member();
                        m.gobid = id;
                    }
                    nmemb.put(id, m);
                }
                int lid = (leader == null) ? -1 : leader.gobid;
                memb = nmemb;
                leader = memb.get(lid);
            } else if (type == PD_LEADER) {
                Member m = memb.get(msg.int32());
                if (m != null)
                    leader = m;
            } else if (type == PD_MEMBER) {
                Member m = memb.get(msg.int32());
                Coord c = null;
                boolean vis = msg.uint8() == 1;
                if (vis)
                    c = msg.coord();
                Color col = msg.color();
                if (m != null) {
                    m.c = c;
                    m.col = col;
                }
            }
        }
    }

    public class Member {
        int gobid;
        Color col = Color.BLACK;
        private Coord c = null;

        public Gob getgob() {
            return (glob.oc.getgob(gobid));
        }

        public Coord getc() {
            Gob gob;
            if ((gob = getgob()) != null)
                return (gob.getc());
            return (c);
        }
    }
}