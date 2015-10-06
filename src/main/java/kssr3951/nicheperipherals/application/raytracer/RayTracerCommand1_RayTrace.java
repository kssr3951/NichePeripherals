package kssr3951.nicheperipherals.application.raytracer;

import static kssr3951.nicheperipherals.application.Const.F0_DOWN;
import static kssr3951.nicheperipherals.application.Const.F1_UP;
import static kssr3951.nicheperipherals.application.Const.F2_NORTH;
import static kssr3951.nicheperipherals.application.Const.F3_SOUTH;
import static kssr3951.nicheperipherals.application.Const.F4_WEST;
import static kssr3951.nicheperipherals.application.Const.F5_EAST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * @author kssr3951
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1.
 * Please check the contents of the license located in http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 * 
 * この MOD は、Minecraft Mod Public License Japanese Transration (MMPL_J) Version 1.0.1 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://tsoft-web.com/nokiyen/minecraft/modding/MMPL_J
 */
public class RayTracerCommand1_RayTrace implements ITurtleCommand {

    public enum CommandDirection { FORWARD, UP, DOWN, LEFT, RIGHT }

    private TurtleSide side;
    private CommandDirection direction;
    private double baselineLength;
    private double azimuth;
    private Object[] args;

    public RayTracerCommand1_RayTrace(TurtleSide side, CommandDirection direction, double baselineLength, double azimuth, Object[] args) {
        this.side = side;
        this.direction = direction;
        this.baselineLength = baselineLength;
        this.azimuth = azimuth;
        this.args = args;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public TurtleCommandResult execute(ITurtleAccess turtle) {
        
        System.out.println("RayTracerCommand1_RayTrace # execute()");
        List<Vec3> vec3List = null;
        {
            if (null == this.args) {
                return TurtleCommandResult.failure();
            } else {
                if (0 == args.length) {
                    // 引数なし
                    //   ～  コマンド名で指定された方向に１回だけRayTraceを実行する
                    System.out.println("★引数なし");
                    vec3List = new ArrayList<Vec3>();
                    vec3List.add(Vec3.createVectorHelper(0, 0, 1));
                } else if (1 == args.length) {
                    // 引数１個
                    //   ～ 同時に複数回のRayTraceを行う方法（その１）
                    //     ”光源”から照射されるRayの向きを３次元ベクトルで指定する。
                    //   １個目　：　照射方向を指定する配列の配列
                    //           {{x1, y1, z1}, {x2, y2, z2},...}
                    //           (x, y, z) = (0, 0, 1)の場合、コマンド名で指定された方向に真っ直ぐRayが照射される。
                    System.out.println("★引数１個");
                    System.out.println("  １個目 ： " + this.args[0].getClass().getName());
                    if (!(this.args[0] instanceof HashMap)) {
                        System.out.println("aaaaa<<< 1 >>>"); //◆
                        return TurtleCommandResult.failure("Expected table.");
                    }
                    vec3List = readVecArgument((HashMap) this.args[0]);
                    if (null == vec3List) {
                        System.out.println("aaaaa<<< 2 >>>"); //◆
                        return TurtleCommandResult.failure("Expected {{x1, y1, z1}, {x2, y2, z2},...}.");
                    }
                    System.out.println("aaaaa<<< 3 >>>"); //◆
                    
                } else if (2 == args.length) {
                    // 引数２個
                    //   ～ 同時に複数回のRayTraceを行う方法（その２：簡易版）
                    //     ”光源”から、コマンド名で指定された方向に指定された距離だけ離れた平面上の点を通過する向きでRayTraceを実行する。
                    //     平面上のパターンが同じ場合、光源からの距離を近づけることで、広範囲を調査でき、遠ざけることで、狭い範囲を（詳細に）調査することができる。、     
                    //   １個目　：　光源からの距離
                    //   ２個目　：　平面上の点の位置の配列。{{x1, y1},{x2, y2},...}
                    //           点の座標が(x, y) = (0, 0)の場合、光源の位置から、コマンド名で指定された方向に真っ直ぐRayが照射される。
                    System.out.println("★引数２個");
                    System.out.println("  １個目 ： " + this.args[0].getClass().getName());
                    System.out.println("  ２個目 ： " + this.args[1].getClass().getName());
                    if (!(this.args[0] instanceof Double && this.args[1] instanceof HashMap)) {
                        return TurtleCommandResult.failure("Expected double, table.");
                    }
                    System.out.println("    ->" + (Double)this.args[0]);
                    
                    vec3List = readVecArgument((HashMap) this.args[1]);
                    if (null == vec3List) {
                        return TurtleCommandResult.failure("Expected double, {{x1, y1}, {x2, y2},...}.");
                    }
                    Double dist = (Double) args[0];
                    for (Vec3 v3 : vec3List) {
                        v3.zCoord = dist;
                    }
                }
            }
        }
        
        int dir;
        
        if (TurtleSide.Left == this.side) {
            final int[] LEFT_TURN;
            {
                int[] tmpL = new int[6];
                tmpL[F0_DOWN ] = F0_DOWN;
                tmpL[F1_UP   ] = F1_UP;
                tmpL[F2_NORTH] = F4_WEST;
                tmpL[F3_SOUTH] = F5_EAST;
                tmpL[F4_WEST ] = F3_SOUTH;
                tmpL[F5_EAST ] = F2_NORTH;
                LEFT_TURN = tmpL;
            }
            dir = LEFT_TURN[turtle.getDirection()];
            
        } else if (TurtleSide.Right == this.side) {
            final int[] RIGHT_TURN;
            {
                int[] tmpR = new int[6];
                tmpR[F0_DOWN ] = F1_UP;
                tmpR[F1_UP   ] = F0_DOWN;
                tmpR[F2_NORTH] = F5_EAST;
                tmpR[F3_SOUTH] = F4_WEST;
                tmpR[F4_WEST ] = F2_NORTH;
                tmpR[F5_EAST ] = F3_SOUTH;
                RIGHT_TURN = tmpR;
            }
            dir = RIGHT_TURN[turtle.getDirection()];
            
        } else {
            System.out.println("adjust side else"); //◆
            return null;
        }
        Vec3 rayStart = Vec3.createVectorHelper(
            turtle.getPosition().posX + 0.5 + Facing.offsetsXForSide[dir],
            turtle.getPosition().posY + 0.5 + Facing.offsetsYForSide[dir],
            turtle.getPosition().posZ + 0.5 + Facing.offsetsZForSide[dir]);
        
        // System.out.println("  vec3List.size() = " + vec3List.size());
        List<Double> resultList = new ArrayList<Double>();
        for (int i = 0; i < vec3List.size(); i++) {
            Vec3 rayDir = vec3List.get(i);
            
            // 以下の分岐の、FORWARDの場合について、
            //   turtle.getDirection() ⇒ {"DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST"};
            //   NORTH = 前進するとzが減少する。
            // * SOUTH = 前進するとzが増加する。
            //   WEST  = 前進するとxが減少する。 
            //   EAST  = 前進するとxが増加する。
            // ⇒ 南向きの場合は加工不要。それ以外の場合はY軸で回転させる。
            final int[] ROT_FORWARD_90_COUNT = new int[]{ 0, 0, 2, 0, 3, 1 };
            
            if (CommandDirection.FORWARD == this.direction) {

                rayDir = rotateAroundY90(rayDir, ROT_FORWARD_90_COUNT[turtle.getDirection()]);

            } else if (CommandDirection.LEFT == this.direction) {

                // FORWARDの状態からさらに左回転
                rayDir = rotateAroundY90(rayDir, ROT_FORWARD_90_COUNT[turtle.getDirection()] + 3);

            } else if (CommandDirection.RIGHT == this.direction) {

                // FORWARDの状態からさらに右回転
                rayDir = rotateAroundY90(rayDir, ROT_FORWARD_90_COUNT[turtle.getDirection()] + 1);

            } else if (CommandDirection.UP == this.direction) {

                switch (turtle.getDirection()) {
                case 2: rayDir = rotateAroundX90(rayDir, 3); break; // NORTH
                case 3: rayDir = rotateAroundX90(rayDir, 1); break; // SOUTH
                case 4: rayDir = rotateAroundZ90(rayDir, 1); break; // WEST
                case 5: rayDir = rotateAroundZ90(rayDir, 3); break; // EAST
                default: break; 
                }
            } else if (CommandDirection.DOWN == this.direction) {

                switch (turtle.getDirection()) {
                case 2: rayDir = rotateAroundX90(rayDir, 1); break; // NORTH
                case 3: rayDir = rotateAroundX90(rayDir, 3); break; // SOUTH
                case 4: rayDir = rotateAroundZ90(rayDir, 3); break; // WEST
                case 5: rayDir = rotateAroundZ90(rayDir, 1); break; // EAST
                default: break; 
                }
            } else {
                System.out.println("<<< 6 >>>");
                return null;
            }

            {
                ChunkCoordinates cod = turtle.getPosition();
                System.out.println("turtle  (x, y, z) = (" + cod.posX + ", " + cod.posY + ", " + cod.posZ + ")"); //◆
                System.out.println("rayStart(x, y, z) = (" + rayStart.xCoord + ", " + rayStart.yCoord + ", " + rayStart.zCoord + ")"); //◆
                System.out.println("rayDir  (x, y, z) = (" + rayDir.xCoord + ", " + rayDir.yCoord + ", " + rayDir.zCoord + ")"); //◆
            }
            double distance = rayTrace(turtle.getWorld(), rayStart, rayDir);
            resultList.add(distance);
        }
        return TurtleCommandResult.success(resultList.toArray());
    }

    private static double rayTrace(World world, Vec3 vecStart, Vec3 vecDir) {
        {
          boolean hit = false;
          Vec3 vS = vecStart.addVector(0, 0, 0);
          Vec3 vecEnd = null;
          double dist = 0.5;
          for (int i = 0; i < 512; i++) { // てきとう
              vecEnd = vS.addVector(vecDir.xCoord * dist, vecDir.yCoord * dist, vecDir.zCoord * dist);
              // ブロック
              {
                  MovingObjectPosition result = world.rayTraceBlocks(vS.addVector(0, 0, 0), vecEnd.addVector(0, 0, 0));

                  if (result != null && result.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK) {
                      double hitDistance = vecStart.distanceTo(result.hitVec);
                      System.out.println("==========================================================");
                      System.out.println("== Block found!!");
                      System.out.println("==========================================================");
                      System.out.println("  rayTraceENtities|1|hit. hitDistance = " + hitDistance); //◆
                      System.out.println("  rayTraceENtities|4|hit. vecStart = (x, y, z) = ("
                              + vecStart.xCoord + ", " + vecStart.yCoord + ", " + vecStart.zCoord + ")"); //◆
                      System.out.println("  rayTraceENtities|5|hit. vS       = (x, y, z) = ("
                              + vS.xCoord + ", " + vS.yCoord + ", " + vS.zCoord + ")"); //◆
                      System.out.println("  rayTraceENtities|6|hit. hitVec   = (x, y, z) = ("
                              + result.hitVec.xCoord + ", " + result.hitVec.yCoord + ", " + result.hitVec.zCoord + ")"); //◆
                      hit = true;
                      return hitDistance;
                  }
              }
              // エンティティ
              {
                  AxisAlignedBB bigBox = AxisAlignedBB.getBoundingBox(
                          vS.xCoord - 0.4,
                          vS.yCoord - 0.4,
                          vS.zCoord - 0.4,
                          vS.xCoord + 0.4,
                          vS.yCoord + 0.4,
                          vS.zCoord + 0.4);

                  Entity closest = null;
                  double closestDist = 99D; // てきとう
                  @SuppressWarnings("rawtypes")
                  List list = world.getEntitiesWithinAABBExcludingEntity(null, bigBox);
                  for (int j = 0; j < list.size(); j++) {
                      Entity entity = (Entity) list.get(j);
                      if (!entity.canBeCollidedWith()) {
                          continue;
                      }
                      AxisAlignedBB littleBox = entity.boundingBox;
                      if (littleBox.isVecInside(vS)) {
                          closest = entity;
                          closestDist = 0.0D;
                          continue;
                      }
                      MovingObjectPosition littleBoxResult = littleBox.calculateIntercept(vS, vecEnd);
                      if (littleBoxResult != null) {
                          double dd = vecStart.distanceTo(littleBoxResult.hitVec);
                          if (closest == null || dd <= closestDist) {
                              closest = entity;
                              closestDist = dd;
                          }
                          continue;
                      }
                      if (littleBox.intersectsWith(bigBox) && closest == null) {
                          closest = entity;
                          closestDist = vecStart.distanceTo(vS);
                      }
                  }
                  if (closest != null && closestDist < 256D) { // てきとう
                      System.out.println("==========================================================");
                      System.out.println("== Entity found!!");
                      System.out.println("==========================================================");
                      System.out.println("  Entity distance = " + closestDist);
                      hit = true;
                      return closestDist;
                  }
              }
              vS = vS.addVector(vecDir.xCoord * dist, vecDir.yCoord * dist, vecDir.zCoord * dist);
          }
          if (!hit) {
              System.out.println("  not found");
          }
        }
        return 512D; // てきとう
    }

    private List<Vec3> readVecArgument(@SuppressWarnings("rawtypes") HashMap h) {
        List<Vec3> rslt = new ArrayList<Vec3>();
        for (int i = 0; i < h.size(); i++) {
            Object o = h.get(new Double(h.size() - i));
            if (!(o instanceof HashMap)) {
                System.out.println("   readVecArgument<<< 1 >>>");
                return null;
            }
            @SuppressWarnings("rawtypes")
            HashMap h2 = (HashMap) o;
            List<Double> tmp = new ArrayList<Double>();
            for (int j = 0; j < h2.size(); j++) {
                Object o2 = h2.get(new Double(j + 1));
                if (!(o2 instanceof Double)) {
                    System.out.println("   readVecArgument<<< 2 >>>");
                    return null;
                } else {
                    tmp.add((Double) o2);
                }
            }
            if (2 == tmp.size()) {
                rslt.add(Vec3.createVectorHelper(tmp.get(0), tmp.get(1), 0));
            } else if(3 == tmp.size()) {
                rslt.add(Vec3.createVectorHelper(tmp.get(0), tmp.get(1), tmp.get(2)));
            } else {
                System.out.println("   readVecArgument<<< 3 >>>");
                return null;
            }
        }
        return rslt;
    }
    
    private Vec3 rotateAroundY90(Vec3 vec3, int rotationCount) {
        // 90度単位の回転なら、要素を入れ替えたほうが誤差が出ないので良い
        Double x = vec3.xCoord;
        Double z = vec3.zCoord;
        
        final Double[] ROT_X = new Double[] { x, z, -x, -z };
        final Double[] ROT_Z = new Double[] { z, -x, -z, x };
        
        return Vec3.createVectorHelper(ROT_X[rotationCount % 4], vec3.yCoord, ROT_Z[rotationCount % 4]);
    }
    
    private Vec3 rotateAroundX90(Vec3 vec3, int rotationCount) {
        Double y = vec3.yCoord;
        Double z = vec3.zCoord;
        
        final Double[] ROT_Y = new Double[] { y, z, -y, -z };
        final Double[] ROT_Z = new Double[] { z, -y, -z, y };
        
        return Vec3.createVectorHelper(vec3.xCoord, ROT_Y[rotationCount % 4], ROT_Z[rotationCount % 4]);
    }

    private Vec3 rotateAroundZ90(Vec3 vec3, int rotationCount) {
        Double x = vec3.xCoord;
        Double y = vec3.zCoord;
        
        final Double[] ROT_X = new Double[] { x, y, -x, -y };
        final Double[] ROT_Y = new Double[] { y, -x, -y, x };
        
        return Vec3.createVectorHelper(ROT_X[rotationCount % 4], ROT_Y[rotationCount % 4], vec3.zCoord);
    }
}
